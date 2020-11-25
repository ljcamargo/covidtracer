package tech.camargo.covid.network

import io.ktor.client.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class AttendanceRepo(private val client : HttpClient) {

    suspend fun getSubmissionSite(url: String): HttpResponse {
        client.cookies("medidassanitarias.covid19.cdmx.gob.mx")
        return client.request(url) {
            method = HttpMethod.Get
        }
    }

    suspend fun submitAttendance(map: Map<String, String>): HttpResponse {
        val url = "https://medidassanitarias.covid19.cdmx.gob.mx/registrar_ciudadano"
        client.cookies("medidassanitarias.covid19.cdmx.gob.mx")
        return client.request(url) {
            method = HttpMethod.Post
            body = FormDataContent(Parameters.build {
                map.forEach { (key, value) -> append(key, value) }
            })
        }
    }


}