package tech.camargo.covid.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.soywiz.klock.DateTime
import com.soywiz.klock.ISO8601
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.camargo.covid.Persistent
import tech.camargo.covid.models.Status
import tech.camargo.covid.models.Attendance

class ResultViewModel: ViewModel(), KoinComponent {

    private val persistent: Persistent by inject()
    private val viewModelJob = SupervisorJob()
    private val background = CoroutineScope(Dispatchers.Default + viewModelJob)

    val attendance = MutableLiveData<Attendance>()
    val status = MutableLiveData<Status>()
    val wait = MutableLiveData<Boolean>()

    private fun now(): String {
        return DateTime.now().format(ISO8601.DATE_CALENDAR_COMPLETE)
    }

    fun submitQR(qr: String) {
        val now = now()
        val phone = persistent.cellphones ?: return
        background.launch {
            wait.postValue(true)
            delay(5000)
            status.postValue(Status(true, 200))
            val new = Attendance(now).apply {
                place = "Lorem Ipsum"
                cellphone = phone
                this.qr = qr
            }
            persistent.addVisit(new)
            wait.postValue(false)
            attendance.postValue(new)
        }
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

}