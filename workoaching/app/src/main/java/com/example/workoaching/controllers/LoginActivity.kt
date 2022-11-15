package com.example.workoaching.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.example.workoaching.R
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.models.LoginData
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

     lateinit var shared : SharedPreferences

     lateinit var validator : AwesomeValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.hide()

        setContentView(R.layout.activity_login)

        shared = getSharedPreferences("login", Context.MODE_PRIVATE)
        val isUserLoggedIn = shared.getBoolean("isUserLoggedIn", false)
        if(isUserLoggedIn) {
            val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
        validator = AwesomeValidation(ValidationStyle.BASIC)
        validator.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.error_email_field)



        buttonRegister.setOnClickListener(this)
        button.setOnClickListener(this)



    }

    override fun onClick(v: View?) {

        when(v!!.id){
            R.id.buttonRegister -> {

                val registerIntent = Intent(this, RegisterActivity::class.java)

                startActivity(registerIntent)
            }
            R.id.button -> login()
        }

    }

    private fun login(){

        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        val loginData = LoginData(
            "login",
            email,
            password
        )

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Boolean> = service.login(loginData)

        result.enqueue(object: Callback<Boolean>{
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Ha ocurrido un error con la conexion", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val result = response.body()?:false

                if(result){
                    val edit = shared.edit()

                    edit.putBoolean("isUserLoggedIn", true)
                    edit.putString("userEmail", loginData.email)
                    edit.apply()

                    val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()

                } else {
                    Toast.makeText(this@LoginActivity, "Usuario y/o contraseña incorrectas", Toast.LENGTH_LONG).show()

                }
            }
        })

    }
}