package org.sandec.hanippund.easyretrofit.listener

import okhttp3.Response

interface InterceptorListener {

    /**
     *
     * This is for custom when your authentication request is null
     *
     * @param response is Response
     * */
    fun onAuthorizationFailed(response: Response)
}