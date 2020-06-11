package org.sandec.hanippund.simpleretrofit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Response
import org.sandec.hanippund.easyretrofit.SimpleRetrofit
import org.sandec.hanippund.easyretrofit.listener.InterceptorListener
import org.sandec.hanippund.simpleretrofit.preference.PreferenceHelper

abstract class BaseActivity : AppCompatActivity() {

    lateinit var simpleRetrofit: SimpleRetrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseUrl = "http:/172.28.1.62:5000/"

        simpleRetrofit = SimpleRetrofit(this)
            .setBaseUrl(baseUrl)
            .setConnectionTimeout(5000)
            .setReadTimeout(5000)
            .setWriteTimeout(5000)
            .setAuthorizationListener(interceptorListener)
            .build()

    }

    val interceptorListener = object : InterceptorListener {
        override fun onAuthorizationFailed(response: Response) {
            PreferenceHelper(this@BaseActivity).succesLogout()

            finishAffinity()
            val intent = Intent(this@BaseActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}