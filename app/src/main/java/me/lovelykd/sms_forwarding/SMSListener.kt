package me.lovelykd.sms_forwarding

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class SMSListener : BroadcastReceiver() {
    private val logTag = "SMSListener"

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            val sharedDefaultPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val barkServerURL =
                sharedDefaultPreferences.getString(Constants.KEY_BARK_SERVER_URL, "")
            if (barkServerURL.isNullOrEmpty()) {
                Log.w(logTag, "Bark server URL is not configured")
                return
            }
            val queue = RequestQueueSingleton.getInstance(context)
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            var originatingAddress = ""
            val messageBodyBuilder = StringBuilder()
            for (message in messages) {
                if (originatingAddress.isEmpty()) {
                    originatingAddress = message.displayOriginatingAddress
                }
                messageBodyBuilder.append(message.displayMessageBody)
            }
            Log.i(logTag, "Received message from $originatingAddress")
            val url = "$barkServerURL/$originatingAddress/$messageBodyBuilder"
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, null, null)
            queue.addToRequestQueue(jsonObjectRequest)
        }
    }

}