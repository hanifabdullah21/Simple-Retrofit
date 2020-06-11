# Simple-Retrofit
Librari ini memudahkan anda dalam menggunakan Retrofit pada Android.

Beberapa fitur yang ada pada librari ini :
  1. Interceptor jika authentication gagal atau tidak dikenal
  2. Interceptor koneksi internet saat melakukan permintaan
  3. Menggunakan bahasa kotlin
  4. Menggunakan Rx
  5. Lebih memudahkan dalam penulisan
  

## Permission
```XML
<uses-permission android:name="android.permission.INTERNET" />
```

## Installing
Tambahkan dependencies di build.gradle

```gradle
dependencies{
  implementation 'com.github.hanifabdullah21:simple-retrofit:$latest_version'
  
  //Tambahkan dependencies retrofit dan rx
  implementation 'com.squareup.retrofit2:retrofit:$retrofit_version'
  implementation 'io.reactivex.rxjava2:rxjava:$rx_version'
}
```

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

##Cara menggunakan
1.  Persiapkan kelas Data Class dan API Interface

    contoh Data Classes:
    ```kotlin
      data class ResponseAuthModel(
      
        @field:SerializedName("result")
        val result: AuthModel? = null,
      
        @field:SerializedName("status")
        val status: StatusModel? = null
      )
    ```

    contoh API Interface:
    ```kotlin
      interface APIEndpoint {
        @FormUrlEncoded
        @POST("login")
        fun loginAccount(
          @Field("email") email: String?): Observable<ResponseAuthModel?>

        @GET("logout")
        fun logoutAccount(
          @Header("Authorization") token: String): Observable<ResponseAuthModel?>

        @GET("profil")
        fun getProfil(
          @Header("Authorization") token: String) : Observable<ResponseAuthModel?>
    }
    ```
2. Atur konfigurasi retrofit
    ```kotlin
    SimpleRetrofit(context)
      .setBaseUrl("url")                              //Wajib
      .setConnectionTimeout(TIME)                     //Opsional. Satuannya adalah detik
      .setReadTimeout(TIME)                           //Opsional. Satuannya adalah detik
      .setWriteTimeout(TIME)                          //Opsional. Satuannya adalah detik
      .setAuthorizationListener(YOUR_AUTH_LISTENER)   //Opsional. Jika anda ingin mengkustom authorization listener
      .addInterceptor(YOUR_INTERCEPTOR)               //Opsional. Jika anda ingin menambahkan interceptor lagi
      .build()                                        //Wajib
    ```
    
    Jika anda ingin menggunakan OkHttpClient anda sendiri, lakukan konfigurasi sebagai berikut
    ```kotlin
    SimpleRetrofit(context)
      .setOkHttpClient(YOUR_OKHTTPCLIENT)
      .build()
    ```

3. Atur layanan API
    ```kotlin
    val api = simpleRetrofit
         .createApiServices(YOUR_CLASS_API_SERVICE)
         .(YOUR_METHOD_SERVICE)
    ```
    
4. Buat Request
    ```kotlin
    simpleRetrofit
        .request(YOUR_API_SERVICE, object : ResponseListener<YOUR_RESULT_DATA_CLASS>{
          override fun onSuccess(model: YOUR_RESULT_DATA_CLASS?) {
                //TODO Lakukan sesuatu jika permintaan anda berhasil
            }

            override fun onFailure(throwable: Throwable, json: String?, message: String?) {
                //Parameter json mungkin akan bernilai null jika permintaan anda tidak dapat dikonvert ke json
                //Gunakan kode di bawah ini jika parameter json tidak null dan anda ingin mengubah json menjadi data class
                val response = Gson().fromJson(json, YOUR_DATA_CLASS)
            }
        })
    ```
