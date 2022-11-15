package com.example.workoaching.controllers

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.workoaching.R
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.models.User
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.ImageUtilities
import com.example.workoaching.viewpageradapters.ViewPagerPublicUserAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_public_user.*
import kotlinx.android.synthetic.main.activity_public_user.pager
import kotlinx.android.synthetic.main.activity_public_user.shapeableImageView
import kotlinx.android.synthetic.main.activity_public_user.tab_layout
import kotlinx.android.synthetic.main.activity_public_user.txtUsername
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.fragment_public_user_routines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PublicUserActivity : AppCompatActivity() {
    lateinit var userEmail : String
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_user)

        userEmail = intent.extras!!.getString("userEmail")?:""


        pager.adapter = ViewPagerPublicUserAdapter(this, userEmail)

        val tabLayoutMediator = TabLayoutMediator(tab_layout, pager, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when(position) {
                0-> {
                    tab.text = getString(R.string.public_user_tabs_routines)

                }
                1-> {
                    tab.text = getString(R.string.public_user_tabs_images)
                }
            }

        })

        tabLayoutMediator.attach()

//        getUserByEmail(userEmail)
        ApiManager.getUserByEmail(userEmail, {response ->

            val user = response.body()
            var byteArray:ByteArray? = null
            if(!user!!.imagen.isNullOrEmpty()){
                byteArray = Base64.getDecoder().decode(user!!.imagen)
                if(byteArray != null){
                    shapeableImageView.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
                }
            }

            txtUsername.text = "${user.nombre} ${user.apellido_p}"
        }, {t ->
            Toast.makeText(this, "Ha ocurrido un error al traer la informacion del usuario", Toast.LENGTH_LONG)
        })

    }

    private fun getUserByEmail(userEmail : String){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)

        val result: Call<User> = service.getUserByEmail(userEmail)

        result.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@PublicUserActivity, "Ha ocurrido un error al traer la informacion del usuario", Toast.LENGTH_LONG)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                var byteArray:ByteArray? = null
                if(!user!!.imagen.isNullOrEmpty()){
                    byteArray = Base64.getDecoder().decode(user!!.imagen)
                    if(byteArray != null){
                        shapeableImageView.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
                    }
                }

                txtUsername.text = "${user.nombre} ${user.apellido_p}"



            }
        })
    }


}