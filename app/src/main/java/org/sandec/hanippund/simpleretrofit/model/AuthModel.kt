package org.sandec.hanippund.simpleretrofit.model

import com.google.gson.annotations.SerializedName

data class AuthModel(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null
)