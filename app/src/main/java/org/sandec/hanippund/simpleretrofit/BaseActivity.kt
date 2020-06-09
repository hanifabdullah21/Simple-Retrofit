package org.sandec.hanippund.simpleretrofit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Response
import org.sandec.hanippund.easyretrofit.listener.AuthorizationListener
import org.sandec.hanippund.easyretrofit.SimpleRetrofit
import org.sandec.hanippund.simpleretrofit.preference.PreferenceHelper
import java.util.concurrent.TimeUnit

abstract class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseUrl = "http://192.168.100.9:5000/"

        /** Use it when you want configuration retrofit is default */
        //retrofitCOnfigDefault(baseUrl)

        /** use this if you want to use an AuthorizationListener modification */
        retrofitCOnfigCustomAuthorizationListener(baseUrl)

        /** use this if you want to use an OkHttpClient modification */
//        retrofitCOnfigCustomOkHttpClient(baseUrl)
    }

    /**
     * Default configuration Retrofit
     *
     * @param baseUrl is Base url to server
     *
     * */
    private fun retrofitCOnfigDefault(baseUrl: String){
        SimpleRetrofit()
            .setBaseUrl(baseUrl)
            .setOkHttpClient()
    }

    /**
     * Configuration Retrofit with custom AuthorizationListener
     *
     * @param baseUrl is Base url to server
     *
     * */
    private fun retrofitCOnfigCustomAuthorizationListener(baseUrl: String) {
        val authorizationListener = object :
            AuthorizationListener {
            override fun onAuthorizationFailed(response: Response) {
                PreferenceHelper(this@BaseActivity).succesLogout()

                finishAffinity()
                val intent = Intent(this@BaseActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        SimpleRetrofit()
            .setBaseUrl(baseUrl)
            .setOkHttpClient(authorizationListener)
    }

    /**
     * Configuration Retrofit with custom OkHttpClient
     *
     * @param baseUrl is Base url to server
     *
     * */
    private fun retrofitCOnfigCustomOkHttpClient(baseUrl: String) {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.SECONDS)
            .build()

        SimpleRetrofit()
            .setBaseUrl(baseUrl)
            .setOkHttpClient(okHttpClient)
    }
}