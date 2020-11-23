package tech.camargo.covid

import android.content.Context
import com.google.zxing.BarcodeFormat
import org.koin.core.KoinComponent

class Constants(val context: Context): KoinComponent {

    fun supportedCodes(): List<BarcodeFormat> {
        return context.resources.getStringArray(R.array.supported_code_formats).map {
            BarcodeFormat.valueOf(it)
        }
    }

}