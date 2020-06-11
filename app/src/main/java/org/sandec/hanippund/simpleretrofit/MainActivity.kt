package org.sandec.hanippund.simpleretrofit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.sandec.hanippund.easyretrofit.SimpleRetrofit
import org.sandec.hanippund.easyretrofit.listener.ResponseListener
import org.sandec.hanippund.simpleretrofit.connection.APIEndpoint
import org.sandec.hanippund.simpleretrofit.model.ResponseAuthModel
import org.sandec.hanippund.simpleretrofit.preference.PreferenceHelper


class MainActivity : BaseActivity(), View.OnClickListener {

    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        token = PreferenceHelper(this).token

        getProfil()

        btn_logout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_logout -> {
                logout()
            }
        }
    }

    private fun logout() {
        val logout = simpleRetrofit.createApiServices(APIEndpoint::class.java)
            .logoutAccount("Bearer $token")

        simpleRetrofit.request(logout, object :
            ResponseListener<ResponseAuthModel?> {
            override fun onSuccess(model: ResponseAuthModel?) {
                Toast.makeText(this@MainActivity, "Logout", Toast.LENGTH_SHORT).show()
                PreferenceHelper(this@MainActivity).succesLogout()
                finishAffinity()
            }

            override fun onFailure(throwable: Throwable, json: String?, message: String?) {

                if (json != null) {
                    val response = Gson().fromJson(json, ResponseAuthModel::class.java)
                    Toast.makeText(this@MainActivity, response.status?.message.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getProfil() {
        val profil = simpleRetrofit.createApiServices(APIEndpoint::class.java)
                .getProfil("Bearer $token")

        simpleRetrofit.request(profil, object :
            ResponseListener<ResponseAuthModel?> {
            override fun onSuccess(model: ResponseAuthModel?) {
                tv_name.text = model?.result?.name
                tv_email.text = model?.result?.email
            }

            override fun onFailure(throwable: Throwable, json: String?, message: String?) {
                val response = Gson().fromJson(json, ResponseAuthModel::class.java)
                if (response == null) {
                    Toast.makeText(this@MainActivity, "$message", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
