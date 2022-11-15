package com.example.workoaching.controllers

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.example.workoaching.R
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.models.ChangePasswordData
import kotlinx.android.synthetic.main.fragment_user_profile_fields.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileFields : Fragment(), View.OnClickListener {


    lateinit var shared : SharedPreferences
    lateinit var validator : AwesomeValidation
    var userEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validator = AwesomeValidation(ValidationStyle.BASIC)




        validator.addValidation(activity,
            R.id.editTextPassword,
            R.id.editTextConfirmPassword,
            R.string.error_no_match_password
        )
        validator.addValidation(activity,
            R.id.editTextPassword, RegexTemplate.NOT_EMPTY,
            R.string.empty_field
        )
        validator.addValidation(activity,
            R.id.editTextPassword, "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%.^&+=]).{8,}$",
            R.string.error_password_field
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_user_profile_fields, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shared = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
        userEmail = shared.getString("userEmail", "error")?:""

        btnChangePassword.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnChangePassword ->changePassword()
        }

    }

    private fun changePassword(){
       val data = ValidateFields()
        if(data != null){
            val service: Service = RestEngine.getRestEngine().create(Service::class.java)

            val result: Call<Boolean> = service.changeUserPassword(data)

            result.enqueue(object: Callback<Boolean> {
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(activity, "No se ha podido actualizar la contraseña", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    val result = response.body()?: false
                    if(result){
                        Toast.makeText(activity, "La contraseña ha sido actualizada con exito", Toast.LENGTH_SHORT).show()
                        editTextOldPassword.text.clear()
                        editTextPassword.text.clear()
                        editTextConfirmPassword.text.clear()
                    } else {
                        Toast.makeText(activity, "No se ha podido actualizar la contraseña", Toast.LENGTH_SHORT).show()

                    }



                }
            })
        }



    }

    private fun ValidateFields() : ChangePasswordData?{

        val oldPassword = editTextOldPassword.text.toString()
        val password = editTextPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()


        var data : ChangePasswordData? = null

        if(validator.validate()) {
            data = ChangePasswordData(
                "changePassword",
                userEmail,
                oldPassword,
                password
            )

        }




        return data
    }

}