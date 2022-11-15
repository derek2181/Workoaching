package com.example.workoaching.controllers

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.workoaching.R
import com.example.workoaching.recycleradapter.RecyclerAdapterUserImages
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.models.Image
import com.example.workoaching.models.User
import com.example.workoaching.models.UserImageData
import com.example.workoaching.models.UserImageProgressData
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.ImageUtilities
import com.example.workoaching.utils.NetworkConnection
import com.example.workoaching.utils.SQLiteHelper
import com.example.workoaching.viewpageradapters.ViewPagerUserProfileAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.fragment_user_profile_fields.*
import kotlinx.android.synthetic.main.fragment_user_profile_images.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class UserActivity  : AppCompatActivity(), View.OnClickListener {

    lateinit var shared : SharedPreferences
    lateinit var userImages : RecyclerView

    val PERMISSION_CODE_PROFILE_PICTURE = 1001
    val PERMISSION_CODE_PROGRESS_PICTURE = 1010
    val CAMERA_CODE = 1002
    val IMAGE_PICK_CODE_PROFILE_PICTURE = 1003
    val IMAGE_PICK_CODE_PROGRESS_PICTURE = 1013

    var userEmail : String = ""
    var userId : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        supportActionBar!!.hide()
        setContentView(R.layout.activity_user)


        shared = getSharedPreferences("login", Context.MODE_PRIVATE)
        userEmail = shared.getString("userEmail", "error")?:"null"

//        getUserByEmail(userEmail

        ApiManager.getUserByEmail(userEmail, {response ->

            val user = response.body()
            var byteArray:ByteArray? = null
            if(!user!!.imagen.isNullOrEmpty()){
                byteArray = Base64.getDecoder().decode(user!!.imagen)
                if(byteArray != null){
                    shapeableImageView.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

                }
            }

            userId = user.id_usuario
            txtUsername.text = "${user.nombre} ${user.apellido_p}"

        }, {t ->
            Toast.makeText(this, "Ha ocurrido un error al traer la informacion del usuario", Toast.LENGTH_LONG)
        })

        val networkConnection= NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer{ isConnected->

            if(!isConnected){
                val intent= Intent(this,MainActivity::class.java)

                startActivity(intent)
            }
        })



        val pagerUser = findViewById<ViewPager2>(R.id.pager)
        pagerUser.adapter = ViewPagerUserProfileAdapter(this)

        val tab_layoutMain = findViewById<TabLayout>(R.id.tab_layout)

        val tabLayoutMediator = TabLayoutMediator(tab_layoutMain, pagerUser, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when(position) {
                0-> {

                    tab.text = getString(R.string.user_tabs_my_progress)


                }
                1-> {

                    tab.text = getString(R.string.user_tabs_account_details)

                }
            }

        })

        tabLayoutMediator.attach()

        btnLogOut.setOnClickListener(this)
        user_change_img_btn.setOnClickListener(this)
        add_img_btn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v!!.id){
            R.id.btnLogOut -> {
                val editor = shared.edit()
                editor.clear()
                editor.apply()



                val loginIntent = Intent(this, LoginActivity::class.java)
                loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(loginIntent)
                finish()
            }
            R.id.user_change_img_btn ->changeImage(IMAGE_PICK_CODE_PROFILE_PICTURE, PERMISSION_CODE_PROFILE_PICTURE)
            R.id.add_img_btn ->changeImage(IMAGE_PICK_CODE_PROGRESS_PICTURE, PERMISSION_CODE_PROGRESS_PICTURE)


        }

    }

    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)

        if (resultcode == Activity.RESULT_OK) {
            //RESPUESTA DE LA CÁMARA CON TIENE LA IMAGEN
            if (requestcode == CAMERA_CODE) {

                val photo =  data?.extras?.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                //Bitmap.CompressFormat agregar el formato desado, estoy usando aqui jpeg
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                shapeableImageView.setImageBitmap(photo)
                //Agregamos al objecto album el arreglo de bytes
//                val albumEdit:Album =  DataManager.albums[albumPosition]
//                albumEdit.imgArray =  stream.toByteArray()
                //Mostramos la imagen en la vista

//                this.imgAlbum.setImageBitmap(photo)

            }

            if(requestcode == IMAGE_PICK_CODE_PROFILE_PICTURE){
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.image_change))
                    .setMessage(getString(R.string.image_change_confirm))
                    .setNegativeButton(getString(R.string.no)){
                            dialog,_->
                        dialog.dismiss()
                    }
                    .setPositiveButton(getString(R.string.yes)){
                            dialog,_->
                        shapeableImageView.setImageURI(data?.data)
                        val bitmap = (shapeableImageView.getDrawable() as BitmapDrawable).bitmap
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val imageBase64 = Base64.getEncoder().encodeToString(baos.toByteArray())
                        changeUserImage(userEmail, imageBase64)

                        dialog.dismiss()
                    }
                    .create()
                    .show()


            }
            if(requestcode == IMAGE_PICK_CODE_PROGRESS_PICTURE){
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.image_add))
                    .setMessage(getString(R.string.image_add_confirm))
                    .setNegativeButton(getString(R.string.no)){
                            dialog,_->
                        dialog.dismiss()
                    }
                    .setPositiveButton(getString(R.string.yes)){
                            dialog,_->
//                        shapeableImageView.setImageURI(data?.data)
//                        val bitmap = (shapeableImageView.getDrawable() as BitmapDrawable).bitmap
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val imageBase64 = Base64.getEncoder().encodeToString(baos.toByteArray())
                        addUserProgressImage(userId, imageBase64)

                        dialog.dismiss()
                    }
                    .create()
                    .show()


            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE_PROFILE_PICTURE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery(IMAGE_PICK_CODE_PROFILE_PICTURE)
                }
                else{
                    //PERMISO DENEGADO
                    Toast.makeText(this, "No se tienen los permisos", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISSION_CODE_PROGRESS_PICTURE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery(IMAGE_PICK_CODE_PROGRESS_PICTURE)
                }
                else{
                    //PERMISO DENEGADO
                    Toast.makeText(this, "No se tienen los permisos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changeUserImage(userEmail: String, image:String){

        val data = UserImageData(
                        "changePicture",
                        userEmail,
                        image

        )

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)

        val result: Call<Boolean> = service.changeUserImage(data)

        result.enqueue(object: Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(this@UserActivity, "No se ha podido actualizar la imagen", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val result = response.body()?: false
                if(result){
                    Toast.makeText(this@UserActivity, "La imagen ha sido actualizada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@UserActivity, "No se ha podido actualizar la imagen", Toast.LENGTH_SHORT).show()

                }



            }
        })
    }

    private fun addUserProgressImage(userId: Int?, image:String){

        val data = UserImageProgressData(
            "addImage",
            userId,
            image

        )

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)

        val result: Call<Int> = service.addImage(data)

        result.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@UserActivity, "No se ha podido actualizar la imagen", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val result = response.body()?: 0
                if(result != 0){
//                    pager.adapter?.notifyDataSetChanged()
                    val addedImage = Image(result, image,userId)

                    val pagerUser = findViewById<ViewPager2>(R.id.pager)

//                    val userImages = (pagerUser.adapter as ViewPagerUserProfileAdapter).userImagesFragment.userImagesRecycler
                    val userImages = (pagerUser.adapter as ViewPagerUserProfileAdapter).userImagesFragment.userImagesRecycler

                    (userImages.adapter as RecyclerAdapterUserImages).images.add(addedImage)
                    userImages.adapter?.notifyDataSetChanged()
                    Toast.makeText(this@UserActivity, "La imagen ha sido agregada ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@UserActivity, "No se ha podido agregar la imagen", Toast.LENGTH_SHORT).show()

                }



            }
        })
    }

    private fun getUserByEmail(userEmail : String){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)

        val result: Call<User> = service.getUserByEmail(userEmail)

        result.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@UserActivity, "Ha ocurrido un error al traer la informacion del usuario", Toast.LENGTH_LONG)
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

                userId = user.id_usuario
                txtUsername.text = "${user.nombre} ${user.apellido_p}"
//                editTextEmail.setText(user.email)
//                editTextName.setText(user.nombre)
//                editTextLastname.setText(user.apellido_p)
//                editTextPhone.setText(user.telefono)
            }
        })
    }

    private fun changeImage(imageCode: Int, permissionCode : Int){
        //check runtime permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var boolDo:Boolean =  false
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, permissionCode)
            }
            else{
                //permission already granted
                boolDo =  true

            }


            if(boolDo == true){
                pickImageFromGallery(imageCode)
            }

        }

    }

    private fun pickImageFromGallery(code : Int) {
        //Abrir la galería
        val intent  =  Intent()
        intent.setAction(Intent.ACTION_PICK);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.type = "image/*"
        //startActivityForResult(Intent.createChooser(intent,"Selecciona"), IMAGE_PICK_CODE)
        startActivityForResult(intent, code)

    }
}