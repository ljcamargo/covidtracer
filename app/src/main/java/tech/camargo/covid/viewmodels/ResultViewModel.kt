package tech.camargo.covid.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soywiz.klock.DateTime
import com.soywiz.klock.ISO8601
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.camargo.covid.Persistent
import tech.camargo.covid.models.Attendance
import tech.camargo.covid.models.Status
import tech.camargo.covid.network.AttendanceRepo
import tech.camargo.covid.network.SMSHelper

class ResultViewModel: ViewModel(), KoinComponent {

    private val api: AttendanceRepo by inject()
    private val sms: SMSHelper by inject()
    private val persistent: Persistent by inject()
    private val viewModelJob = SupervisorJob()
    private val background = CoroutineScope(Dispatchers.Default + viewModelJob)

    val attendance = MutableLiveData<Attendance>()
    val status = MutableLiveData<Status>()
    val wait = MutableLiveData<Boolean>()

    private fun now(): String {
        return DateTime.now().format(ISO8601.DATE_CALENDAR_COMPLETE)
    }

    private fun fail(message: String, code: Int) {
        wait.postValue(false)
        status.postValue(Status(false, code).also { it.message = message })
    }

    fun submitQR(qr: String) {
        val now = now()
        val phone = persistent.cellphones ?: return
        background.launch {
            wait.postValue(true)
            val site = api.getSubmissionSite(qr)
            if (site.status.value !in (200..299)) {
                fail(site.status.description, site.status.value)
                return@launch
            }
            val inputs = api.findHiddenInputs(site.readText()).apply {
                put("telefono", phone)
            }
            val post = api.submitAttendance(inputs)
            val redirect = post.headers["Location"]
            val result = if (redirect != null) api.getSubmissionSite(redirect) else post
            if (result.status.value !in (200..299)) {
                fail(result.status.description, result.status.value)
                return@launch
            }
            val info = api.findRegistryInfo(result.readText())
            val new = Attendance(now).fill(phone, qr, info)
            persistent.addVisit(new)
            wait.postValue(false)
            status.postValue(Status(true, 200))
            attendance.postValue(new)
        }
    }

    private fun Attendance.fill(phone: String, qr: String, map: Map<String, String>): Attendance {
        return this.apply {
            id = map["Folio de registro:"]
            place = map["Nombre:"]
            address = map["Domicilio:"]
            metadata = map.map { (key, value) -> "$key: $value" }.joinToString("\n")
            cellphone = phone
            this.qr = qr
        }
    }

    fun submitCode(code: String) {
        val now = now()
        background.launch {
            wait.postValue(true)
            Log.v(TAG,"SMS >> presend sms")
            val (success, error) = sms.sendSMS(sender = null, message = code)
            Log.v(TAG,"SMS >> postsend sms $success, $error")
            if (success) {
                Log.v(TAG,"SMS >> success")
                status.postValue(Status(true, 200))
                val new = Attendance(now).apply {
                    place = code
                    this.code = code
                }
                persistent.addVisit(new)
                attendance.postValue(new)
            } else {
                Log.v(TAG,"SMS >> err")
                status.postValue(Status(false, error))
            }
            Log.v(TAG,"SMS >> end")
            wait.postValue(false)
        }
    }

    companion object {
        const val TAG = "ResultViewModel"
    }

}