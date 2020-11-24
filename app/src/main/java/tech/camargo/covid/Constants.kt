package tech.camargo.covid

import android.content.Context
import android.content.res.Resources
import com.google.zxing.BarcodeFormat
import org.koin.core.KoinComponent

class Constants(val context: Context): KoinComponent {

    fun phonePattern(): String {
        return pattern(R.string.raw_phone_regex)
    }

    fun codePattern(): String {
        return pattern(R.string.manual_code_regex)
    }

    fun qrTextPattern(): String {
        return pattern(R.string.qr_text_regex)
    }

    private fun pattern(id: Int): String {
        return context.getString(id)
    }

    fun supportedCodes(): List<BarcodeFormat> {
        return context.resources.getStringArray(R.array.supported_code_formats).map {
            BarcodeFormat.valueOf(it)
        }
    }

    fun getURI(name: String?): String? {
        name ?: return null
        return context.resources.getStringById("${name}_uri")
    }

    private fun Resources.getStringById(name: String): String? {
        val id = this.getIdentifier(name, "String", context.packageName)
        return if (id > 0) {
            this.getString(id)
        } else {
            null
        }
    }

}