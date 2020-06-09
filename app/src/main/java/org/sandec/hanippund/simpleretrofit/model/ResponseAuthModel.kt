package org.sandec.hanippund.simpleretrofit.model

import com.google.gson.annotations.SerializedName

data class ResponseAuthModel(

    @field:SerializedName("result")
    val result: AuthModel? = null,

    @field:SerializedName("status")
    val status: StatusModel? = null
)