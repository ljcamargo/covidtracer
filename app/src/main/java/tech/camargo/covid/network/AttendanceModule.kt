package tech.camargo.covid.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.cookies.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import org.koin.dsl.module

val network = module {
    single { ktorClient }
    single { AttendanceRepo(get()) }
}

private val ktorClient = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.HEADERS
    }
    install(HttpCookies) {
        storage = AcceptAllCookiesStorage()
    }
}