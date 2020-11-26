package tech.camargo.covid.network

import io.ktor.client.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.camargo.covid.Constants

class AttendanceRepo(private val client : HttpClient): KoinComponent {

    private val constants: Constants by inject()

    suspend fun getSubmissionSite(url: String): HttpResponse {
        client.cookies(constants.cookiesUrl())
        return client.request(url) {
            method = HttpMethod.Get
        }
    }

    suspend fun submitAttendance(map: Map<String, String>): HttpResponse {
        client.cookies(constants.cookiesUrl())
        return client.request(constants.registerUrl()) {
            method = HttpMethod.Post
            body = FormDataContent(Parameters.build {
                map.forEach { (key, value) -> append(key, value) }
            })
        }
    }

    fun findHiddenInputs(text: String): MutableMap<String, String> {
        val regex = hiddenInputs.toRegex()
        return regex.findAll(text).map {
            it.groupValues[1] to it.groupValues[2]
        }.toMap().toMutableMap()
    }

    fun findRegistryInfo(text: String): Map<String, String> {
        val regex = registryInfo.toRegex()
        return regex.findAll(text).map {
            it.groupValues[1] to it.groupValues[2]
        }.toMap()
    }

    companion object {
        const val hiddenInputs = "<input type=\"hidden\" name=\"(\\w+)\" value=(?:\"*)(\\w*)(?:\"*)>"
        const val registryInfo = "<span>(.+)<\\/span> (.+)<\\/div>"
    }

}