package me.lovelykd.sms_forwarding

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class RequestQueueSingleton private constructor(context: Context) {

    private var queue: RequestQueue? = null

    init {
        queue = Volley.newRequestQueue(context)
    }

    companion object {

        @Volatile
        private var INSTANCE: RequestQueueSingleton? = null

        fun getInstance(context: Context): RequestQueueSingleton =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: createInstance(context).also { INSTANCE = it }
            }

        private fun createInstance(context: Context): RequestQueueSingleton {
            return RequestQueueSingleton(context)
        }
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        queue?.add(request)
    }

}