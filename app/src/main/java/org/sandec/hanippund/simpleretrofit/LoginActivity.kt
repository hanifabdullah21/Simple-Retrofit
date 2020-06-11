package org.sandec.hanippund.simpleretrofit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import org.sandec.hanippund.easyretrofit.SimpleRetrofit
import org.sandec.hanippund.easyretrofit.listener.ResponseListener
import org.sandec.hanippund.simpleretrofit.connection.APIEndpoint
import org.sandec.hanippund.simpleretrofit.model.ResponseAuthModel
import org.sandec.hanippund.simpleretrofit.preference.PreferenceHelper

class LoginActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (PreferenceHelper(this).isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btn_login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_login -> {
                if (edt_email.text.isNullOrBlank() || edt_email.text.isNullOrEmpty()) {
                    edt_email.requestFocus()
                    edt_email.error = "Email tidak boleh kosong"
                    return
                }

                login()
            }
        }
    }

    private fun login() {
        val login = SimpleRetrofit(this).createApiServices(APIEndpoint::class.java)
            .loginAccount(edt_email.text.toString())

        simpleRetrofit
            .request(login, object : ResponseListener<ResponseAuthModel?> {
                override fun onSuccess(model: ResponseAuthModel?) {
                    PreferenceHelper(this@LoginActivity)
                        .succesLogin(model?.result?.token.toString())

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }

                override fun onFailure(throwable: Throwable, json: String?, message: String?) {
                    if (json != null) {
                        val response = Gson().fromJson(json, ResponseAuthModel::class.java)
                        Toast.makeText(this@LoginActivity, response.status?.message.toString(), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}
