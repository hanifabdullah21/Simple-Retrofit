package org.sandec.hanippund.easyretrofit

import android.content.Context
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.sandec.hanippund.easyretrofit.config.RetrofitConfig
import org.sandec.hanippund.easyretrofit.listener.InterceptorListener
import org.sandec.hanippund.easyretrofit.listener.ResponseListener
import org.sandec.hanippund.easyretrofit.util.TAG_RETROFIT
import org.sandec.hanippund.easyretrofit.util.checkConnextion
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

class SimpleRetrofit(var context: Context) {

    private var baseUrl: String = ""

    private var interceptorListener: InterceptorListener = object :
        InterceptorListener {
        override fun onAuthorizationFailed(response: Response) {
            Log.d(TAG_RETROFIT, "onAuthorizationFailed $response")
        }
    }

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())

    private var connectionTimeout: Long = 10000
    private var readTimeout: Long = 10000
    private var writeTimeout: Long = 10000

    /**
     *  This function to set BASE URL on Retrofit connection
     *
     *  @param baseUrl is Base URL
     * */
    fun setBaseUrl(baseUrl: String): SimpleRetrofit {
        this.baseUrl = baseUrl
        return this
    }

    /**
     * This function to set the Authorization Listener when want to custom
     *
     * @param interceptorListener is the custom listener
     * */
    fun setAuthorizationListener(interceptorListener: InterceptorListener): SimpleRetrofit {
        this.interceptorListener = interceptorListener
        return this
    }

    /**
     * This function to set the connection timeout
     *
     * @param seconds is the time
     * */
    fun setConnectionTimeout(seconds: Long): SimpleRetrofit {
        connectionTimeout = seconds
        return this
    }

    /**
     * This function to set the read timeout
     *
     * @param seconds is the time
     * */
    fun setReadTimeout(seconds: Long): SimpleRetrofit {
        readTimeout = seconds
        return this
    }

    /**
     * This function to set the write timeout
     *
     * @param seconds is the time
     * */
    fun setWriteTimeout(seconds: Long): SimpleRetrofit {
        writeTimeout = seconds
        return this
    }

    /**
     * This function is to add interceptor to okHttpClient
     *
     * @param interceptor is interceptor
     * */
    fun addInterceptor(interceptor: Interceptor): SimpleRetrofit {
        okHttpClient.addInterceptor(interceptor)
        return this
    }

    /**
     * This function when user want to custom okHttpClient
     *
     * @param okHttpClient is custom okHttpClient
     * */
    fun setOkHttpClient(okHttpClient: OkHttpClient.Builder): SimpleRetrofit {
        this.okHttpClient = okHttpClient
        return this
    }

    /**
     * This function to build all retrofit configuration who has set
     *
     * */
    fun build(): SimpleRetrofit {
        okHttpClient
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val response = chain.proceed(request)

                    //Check internet connection
                    if (response.code() == 401) {
                        interceptorListener.onAuthorizationFailed(response)
                    }
                    return response
                }
            })
            .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)

        RetrofitConfig.setBaseUrl(baseUrl)
            .setOkHttpClient(okHttpClient.build())

        return this
    }

    /**
     * This function to create request for retrofit
     *
     * @param observable the service want to access
     * @param listener the listener to set the response
     * */
    fun <T> request(observable: Observable<T>, listener: ResponseListener<T>) {
        CompositeDisposable().add(
            observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listener.onSuccess(it)
                }, {
                    when (it) {
                        is HttpException -> {
                            try {
                                val body = it.response()?.errorBody()
                                listener.onFailure(it, body?.string(), it.message)
                            } catch (ex: Exception) {
                                listener.onFailure(it, null, it.message)
                            }
                        }
                        is IOException -> {
                            if (!(context.checkConnextion())) {
                                listener.onFailure(it, null, "No Internet")
                            } else {
                                listener.onFailure(it, null, it.message)
                            }
                        }
                        else -> listener.onFailure(it, null, it.message)
                    }
                })
        )
    }

    /**
     * This function to create the API Service for the retrofit
     *
     * @param service is service of Interface
     *
     * @return The class interface
     * */
    fun <T> createApiServices(service: Class<T>?): T {
        return RetrofitConfig.createService(service)
    }
}