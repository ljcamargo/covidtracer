package tech.camargo.covid.models

import kotlinx.serialization.Serializable

@Serializable
data class Attendance(val date: String) {
    val uuid: String = java.util.UUID.randomUUID().toString()
    var cellphone: String? = null
    var id: String? = null
    var place: String? = null
    var address: String? = null
    var metadata: String? = null
    var qr: String? = null
    var code: String? = null
    var email: String? = null
    var city: String? = null
    var state: String? = null
    var lat: Long? = null
    var long: Long? = null

    override fun toString(): String {
        return "id: $id, place: $place, phone: $cellphone"
    }
}