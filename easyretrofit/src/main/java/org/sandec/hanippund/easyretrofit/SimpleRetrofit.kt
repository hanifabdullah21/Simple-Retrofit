package org.sandec.hanippund.easyretrofit

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.sandec.hanippund.easyretrofit.config.RetrofitConfig
import org.sandec.hanippund.easyretrofit.listener.AuthorizationListener
import org.sandec.hanippund.easyretrofit.listener.ResponseListener
import org.sandec.hanippund.easyretrofit.util.FailureHandling
import org.sandec.hanippund.easyretrofit.util.SchedulerProvider
import org.sandec.hanippund.easyretrofit.util.TAG_RETROFIT
import java.io.IOException

class SimpleRetrofit {

    fun setBaseUrl(baseUrl: String): SimpleRetrofit {
        RetrofitConfig.setBaseUrl(baseUrl)
        return this
    }

    private var authorizationListener: AuthorizationListener = object :
        AuthorizationListener {
        override fun onAuthorizationFailed(response: Response) {
            Log.d(TAG_RETROFIT, "onAuthorizationFailed $response")
        }
    }

    fun setOkHttpClient(): SimpleRetrofit {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val response = chain.proceed(request)

                    if (response.code() == 401) {
                        authorizationListener.onAuthorizationFailed(response)
                        return response
                    }
                    return response
                }
            })
            .build()
        RetrofitConfig.setOkHttpClient(okHttpClient)
        return this
    }

    fun setOkHttpClient(listener: AuthorizationListener): SimpleRetrofit {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val response = chain.proceed(request)

                    if (response.code() == 401) {
                        listener.onAuthorizationFailed(response)
                        return response
                    }
                    return response
                }
            })
            .build()

        RetrofitConfig.setOkHttpClient(okHttpClient)
        return this
    }

    fun setOkHttpClient(okHttpClient: OkHttpClient): SimpleRetrofit {
        RetrofitConfig.setOkHttpClient(okHttpClient)
        return this
    }

    fun <T> request(observable: Observable<T>, listener: ResponseListener<T>) {
        CompositeDisposable().add(
            observable.compose(
                SchedulerProvider().ioToMainObservableScheduler()
            )
                .subscribe({
                    listener.onSuccess(it)
                }, {
                    val json = FailureHandling.onFailureRequestHandling(it)
                    listener.onFailure(it, json)
                })
        )
    }

    fun <T> createApiServices(service: Class<T>?): T {
        return RetrofitConfig.createService(service)
    }
}