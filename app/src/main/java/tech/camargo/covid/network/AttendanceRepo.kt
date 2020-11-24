package tech.camargo.covid.network

import androidx.lifecycle.MutableLiveData
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import tech.camargo.covid.models.Attendance

class AttendanceRepo(private val client : HttpClient) {

    suspend fun getSubmissionSite(url: String): String {
        return client.request(url) {
            method = HttpMethod.Get
            /*headers {
                append("My-Custom-Header", "HeaderValue")
            }*/
        }
    }

    suspend fun submitAttendance(
        map: Map<String, String>,
        status : MutableLiveData<Result<Attendance>>
    ) {
        val url = ""
        return client.request(url) {
            method = HttpMethod.Post
            /*headers {
                append("My-Custom-Header", "HeaderValue")
            }*/
        }
    }


}