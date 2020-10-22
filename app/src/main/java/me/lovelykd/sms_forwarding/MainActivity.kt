package me.lovelykd.sms_forwarding

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager


const val EXTRA_MESSAGE = "me.lovelykd.sms_forwarding.MESSAGE"
const val MY_REQUEST_CODE = 10
val ALL_PERMISSIONS = arrayOf(
    Manifest.permission.RECEIVE_SMS,
    Manifest.permission.READ_SMS,
    Manifest.permission.INTERNET
)

class MainActivity : AppCompatActivity() {

    private lateinit var sharedDefaultPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        checkPermission()
        sharedDefaultPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        super.onCreate(savedInstanceState)
        val forwardURL = getForwardURL()
        setContentView(R.layout.activity_main)
        if (!forwardURL.isNullOrEmpty()) {
            findViewById<TextView>(R.id.editForwardURL).apply {
                text = forwardURL
            }
        }
    }

    fun startInfoActivity(view: View) {
        val forwardURL = findViewById<EditText>(R.id.editForwardURL).text.toString()
        setForwardURL(forwardURL)
        val intent = Intent(this, InfoActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, forwardURL)
        }
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        if (!hasAllPermissions()) {
            requestPermissions(ALL_PERMISSIONS, MY_REQUEST_CODE)
        }
    }

    private fun hasAllPermissions(): Boolean {
        for (p in ALL_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun getForwardURL(): String? {
        return sharedDefaultPreferences.getString(Constants.KEY_BARK_SERVER_URL, "")
    }

    private fun setForwardURL(forwardURL: String) {
        with(sharedDefaultPreferences.edit()) {
            putString(Constants.KEY_BARK_SERVER_URL, forwardURL)
            apply()
        }
    }
}