package tech.camargo.covid

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.KoinComponent
import tech.camargo.covid.models.Visit

class Persistent(context: Context): KoinComponent {

    private val storage: SharedPreferences by lazy {
        val name = context.getString(R.string.preferences_name)
        val mode = Context.MODE_PRIVATE
        context.getSharedPreferences(name, mode)
    }

    var cellphones: String?
        get() = storage.getString(CELLPHONES, null)
        set(value) { storage.edit().putString(CELLPHONES, value).apply() }

    var visits: List<Visit>
        get() = Json.decodeFromString(list ?: "[]")
        set(value) { list = Json.encodeToString(value) }

    private var list: String?
        get() = storage.getString(LIST, null)
        set(value) { storage.edit().putString(LIST, value).apply() }

    var disclaimer: Int
        get() = storage.getInt(DISCLAIMER, -1)
        set(value) { storage.edit().putInt(DISCLAIMER, value).apply() }

    var tutorial: Boolean
        get() = storage.getBoolean(TUTORIAL, false)
        set(value) { storage.edit().putBoolean(TUTORIAL, value).apply() }

    val firstTime: Boolean
        get() = cellphones == null


    fun addVisit(visit: Visit) {
        visits = visits + visit
    }

    fun resetList() {
        list = null
    }

    fun reset() {
        cellphones = null
        list = null
        disclaimer = -1
        tutorial = false
    }

    companion object {
        const val CELLPHONES = "cellphones"
        const val DISCLAIMER = "disclaimer"
        const val TUTORIAL = "tutorial"
        const val LIST = "list"
    }
}