package tech.camargo.covid.network

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import android.util.Log
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.camargo.covid.Constants
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class SMSHelper(private val context: Context): KoinComponent {

    private val constants: Constants by inject()

    suspend fun sendSMS(sender: String?, message: String): Pair<Boolean, Int> = suspendCoroutine {
        coroutine ->
        (SmsManager.getDefault() as SmsManager).apply {
            Log.v(TAG, "SMS -> target:${constants.smsTarget()} sender:$sender message:$message")
            sendTextMessage(
                constants.smsTarget(),
                sender,
                message,
                PendingIntent.getBroadcast(context, 0, Intent(SENT), 0),
                null
            )
        }
        context.registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(baseContext: Context?, intent: Intent?) {
                Log.v(TAG, "SMS -> received code $resultCode $resultData")
                val response = when (resultCode) {
                    Activity.RESULT_OK -> true to -1
                    else -> false to resultCode
                }
                context.unregisterReceiver(this)
                coroutine.resume(response)
            }
        }, IntentFilter(SENT))
    }

    companion object {
        const val TAG = "SMSHelper"
        const val SENT = "SMS_SENT"
    }
}