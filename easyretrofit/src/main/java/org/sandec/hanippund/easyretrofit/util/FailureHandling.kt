package org.sandec.hanippund.easyretrofit.util

object FailureHandling {

    /**
     *
     * This function is used to process failure response
     *
     * @param e is the throwable obtained from the failure retrofit response
     *
     * @return json in the form of string when the response can convert, if else is null
     * */
    fun onFailureRequestHandling(e: Throwable): String? {
        when (e) {
            is retrofit2.HttpException -> {
                return try {
                    val body = e.response()?.errorBody()
                    body?.string()
                } catch (ex: Exception) {
                    null
                }
            }
            else -> {
                return null
            }
        }
    }
}