package tech.camargo.covid.models

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val success: Boolean,
    val code: Int
) {
    var message: String? = null
    var request: String? = null

    override fun toString(): String {
        return "success: $success, code: $code, message: $message"
    }
}