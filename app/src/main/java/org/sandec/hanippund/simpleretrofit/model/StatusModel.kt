package org.sandec.hanippund.simpleretrofit.model

import com.google.gson.annotations.SerializedName

data class StatusModel(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)