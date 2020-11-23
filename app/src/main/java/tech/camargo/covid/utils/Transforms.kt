package tech.camargo.covid.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.soywiz.klock.ISO8601
import com.soywiz.klock.KlockLocale
import com.soywiz.klock.parse

object Transforms {

    @JvmStatic
    @BindingAdapter("date")
    fun dateToString(textView: TextView, string: String?) {
        string ?: return
        val date = ISO8601.DATE_CALENDAR_COMPLETE.parse(string)
        val locale = KlockLocale.default
        textView.text = locale.formatDateTimeMedium.format(date)
    }
}