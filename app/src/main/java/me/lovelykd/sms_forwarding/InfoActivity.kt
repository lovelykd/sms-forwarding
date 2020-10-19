package me.lovelykd.sms_forwarding

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val url = intent.getStringExtra(EXTRA_MESSAGE)
        findViewById<TextView>(R.id.textView2).apply {
            text = url
        }
    }
}