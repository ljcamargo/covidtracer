package tech.camargo.covid.models

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val success: Boolean,
    val code: Int
) {
    var err: String? = null
    var message: String? = null
    var log: String? = null
    var date: String? = null
    var request: String? = null
}