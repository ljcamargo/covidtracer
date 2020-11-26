package tech.camargo.covid

import android.content.Context
import android.content.res.Resources
import com.google.zxing.BarcodeFormat
import org.koin.core.KoinComponent

class Constants(val context: Context): KoinComponent {

    fun phonePattern() = pattern(R.string.raw_phone_regex)
    fun codePattern() = pattern(R.string.manual_code_regex)
    fun qrTextPattern() = pattern(R.string.qr_text_regex)
    fun smsTarget() = context.getString(R.string.sms_target)
    fun cookiesUrl() = context.getString(R.string.cookies_url)
    fun registerUrl() = context.getString(R.string.register_url)

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
        val id = this.getIdentifier(name, "string", context.packageName)
        return if (id > 0) {
            this.getString(id)
        } else {
            null
        }
    }

}