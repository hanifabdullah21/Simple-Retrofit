package org.sandec.hanippund.easyretrofit.listener

interface ResponseListener<T> {
    /**
     * The Response listener when response is success
     *
     * @param model is the model of request
     *
     * */
    fun onSuccess(model: T)

    /**
     *
     * The Response listener when response is failure
     *
     * @param throwable is the failure throwable from retrofit
     * @param json is the response when success convert to json, if not maybe is null
     * */
    fun onFailure(throwable: Throwable, json: String?)
}