package tech.camargo.covid.utils

import android.content.Context
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.camargo.covid.Constants

class Linter(val context: Context): KoinComponent {

    private val constants: Constants by inject()

    fun lintPhone(number: String?): Boolean {
        return number?.isNotEmpty() == true
    }

    fun lintCode(text: String?): Boolean {
        return text?.isNotEmpty() == true
    }

    fun lintQR(text: String?): Boolean {
        return text?.isNotEmpty() == true
    }
}