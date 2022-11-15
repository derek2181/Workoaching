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
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.workoaching.R
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.models.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editTextEmail
import kotlinx.android.synthetic.main.activity_register.editTextPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var shared : SharedPreferences

    lateinit var validator : AwesomeValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.hide()

        shared = getSharedPreferences("login", Context.MODE_PRIVATE)

        setContentView(R.layout.activity_register)

        validator = AwesomeValidation(ValidationStyle.BASIC)


        validator.addValidation(this,
            R.id.editTextEmail, RegexTemplate.NOT_EMPTY,
            R.string.empty_field
        )
        validator.addValidation(this,
            R.id.editTextEmail,Patterns.EMAIL_ADDRESS,
            R.string.error_email_field
        )
        validator.addValidation(this,
            R.id.editTextName, RegexTemplate.NOT_EMPTY,
            R.string.empty_field
        )
        validator.addValidation(this,
            R.id.editTextLastname, RegexTemplate.NOT_EMPTY,
            R.string.empty_field
        )
        validator.addValidation(this,
            R.id.editTextPassword,
            R.id.editTextConfirmPassword,
            R.string.error_no_match_password
        )
        validator.addValidation(this,
            R.id.editTextPassword, RegexTemplate.NOT_EMPTY,
            R.string.empty_field
        )
        validator.addValidation(this,
            R.id.editTextPassword, "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%.^&+=]).{8,}$",
            R.string.error_password_field
        )

        btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnRegister ->Register()
        }
    }

    private fun Register(){

        val user = ValidateFields()

        if(user != null){
            val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<Int> = service.register(user)

            result.enqueue(object: Callback<Int> {
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Ha ocurrido un error con la conexion", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    val result = response.body()?:false

                    if(result == 1){
                        val edit = shared.edit()

                        edit.putBoolean("isUserLoggedIn", true)
                        edit.putString("userEmail", user.email)
                        edit.apply()

                        val mainIntent = Intent(this@RegisterActivity, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()

                    } else if (result == 2) {
//                        Hacer que se muestre el error del correo
                        editTextEmail.error = "El correo electronico ya se encuentra en uso"

                    } else {
                        Toast.makeText(this@RegisterActivity, "Ha ocurrido un error al hacer el registro", Toast.LENGTH_LONG).show()

                    }
                }
            })
        }




    }

    private fun ValidateFields() : User?{

        val name = editTextName.text.toString()
        val lastname = editTextLastname.text.toString()
        val secondlastname = editTextSecondLastname.text.toString()
        val email = editTextEmail.text.toString()
        val phone = editTextPhone.text.toString()
        val password = editTextPassword.text.toString()


        var user : User? = null

        if(validator.validate()){

            user = User(
                null,
                email,
                name,
                lastname,
                secondlastname,
                phone,
                null,
                null,
                password
            )


        }






        return user
    }
}