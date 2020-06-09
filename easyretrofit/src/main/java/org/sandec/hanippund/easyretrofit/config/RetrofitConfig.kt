package org.sandec.hanippund.easyretrofit.config

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig{

    private val gson = GsonBuilder().setLenient().create()

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder().build()

    private var baseUrl : String? = null

    fun setBaseUrl(baseUrl: String) : RetrofitConfig {
        RetrofitConfig.baseUrl = baseUrl
        return this
    }

    fun setOkHttpClient(okHttpClient: OkHttpClient): RetrofitConfig {
        RetrofitConfig.okHttpClient = okHttpClient
        return this
    }

    private fun setConnection(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl.toString())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    fun <T> createService(service: Class<T>?): T {
        return setConnection()
            .create(service!!)
    }

}