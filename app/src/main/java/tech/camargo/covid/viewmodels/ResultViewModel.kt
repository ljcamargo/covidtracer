package tech.camargo.covid.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soywiz.klock.DateTime
import com.soywiz.klock.ISO8601
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.camargo.covid.Persistent
import tech.camargo.covid.models.Attendance
import tech.camargo.covid.models.Status
import tech.camargo.covid.network.AttendanceRepo

class ResultViewModel: ViewModel(), KoinComponent {

    private val api: AttendanceRepo by inject()
    private val persistent: Persistent by inject()
    private val viewModelJob = SupervisorJob()
    private val background = CoroutineScope(Dispatchers.Default + viewModelJob)

    val attendance = MutableLiveData<Attendance>()
    val status = MutableLiveData<Status>()
    val wait = MutableLiveData<Boolean>()

    private fun now(): String {
        return DateTime.now().format(ISO8601.DATE_CALENDAR_COMPLETE)
    }

    fun fail(message: String, code: Int) {
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
            val inputs = findHiddenInputs(site.readText()).apply {
                put("telefono", phone)
            }
            Log.v(TAG, "submitQR -> got inputs $inputs")
            val post = api.submitAttendance(inputs)
            val redirect = post.headers["Location"]
            val result = if (redirect != null) api.getSubmissionSite(redirect) else post
            if (result.status.value !in (200..299)) {
                fail(result.status.description, result.status.value)
                return@launch
            }
            val info = findRegistryInfo(result.readText())
            val new = Attendance(now).apply {
                id = info["Folio de registro:"]
                place = info["Nombre:"]
                address = info["Domicilio:"]
                metadata = info.map { (key, value) -> "$key: $value" }.joinToString("\n")
                cellphone = phone
                this.qr = qr
            }
            persistent.addVisit(new)
            wait.postValue(false)
            status.postValue(Status(true, 200))
            attendance.postValue(new)
        }
    }

    private fun findHiddenInputs(text: String): MutableMap<String, String> {
        val regex = "<input type=\"hidden\" name=\"(\\w+)\" value=(?:\"*)(\\w*)(?:\"*)>".toRegex()
        return regex.findAll(text).map {
            it.groupValues[1] to it.groupValues[2]
        }.toMap().toMutableMap()
    }

    private fun findRegistryInfo(text: String): Map<String, String> {
        val regex = "<span>(.+)<\\/span> (.+)<\\/div>".toRegex()
        return regex.findAll(text).map {
            it.groupValues[1] to it.groupValues[2]
        }.toMap()
    }

    fun submitCode(code: String) {
        val now = now()
        background.launch {
            wait.postValue(true)
            delay(5000)
            status.postValue(Status(true, 200))
            val new = Attendance(now).apply {
                place = "Lorem Ipsum"
                this.code = code
            }
            persistent.addVisit(new)
            wait.postValue(false)
            attendance.postValue(new)
        }
    }

    companion object {
        const val TAG = "ResultViewModel"
    }

}