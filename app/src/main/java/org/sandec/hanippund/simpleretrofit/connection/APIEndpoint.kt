package org.sandec.hanippund.simpleretrofit.connection

import io.reactivex.Observable
import org.sandec.hanippund.simpleretrofit.model.ResponseAuthModel
import retrofit2.http.*

interface APIEndpoint {
    @FormUrlEncoded
    @POST("login")
    fun loginAccount(
        @Field("email") email: String?
    ): Observable<ResponseAuthModel?>

    @GET("logout")
    fun logoutAccount(
        @Header("Authorization") token: String
    ): Observable<ResponseAuthModel?>

    @GET("profil")
    fun getProfil(
        @Header("Authorization") token: String
    ) : Observable<ResponseAuthModel?>
}