package tech.camargo.covid.utils

import android.content.Context
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.camargo.covid.Constants


class Linter(val context: Context): KoinComponent {

    private val constants: Constants by inject()

    fun lintPhone(number: String?): Boolean {
        return number?.isNotEmpty() == true && number.matches(constants.phonePattern())
    }

    fun lintCode(text: String?): Boolean {
        return text?.isNotEmpty() == true && text.matches(constants.codePattern())
    }

    fun lintQR(text: String?): Boolean {
        return text?.isNotEmpty() == true && text.matches(constants.qrTextPattern())
    }

    private fun String.matches(pattern: String): Boolean {
        val regex = pattern.toRegex()
        return regex.containsMatchIn(this)
    }
}