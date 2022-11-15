package com.example.workoaching.controllers

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.workoaching.DAO.DatabaseInfo
import com.example.workoaching.R
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.models.FavoriteRoutineData
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.NetworkConnection
import com.example.workoaching.utils.SQLiteHelper
import kotlinx.android.synthetic.main.action_bar.*
import kotlinx.android.synthetic.main.activity_routine_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class RoutineDetailsActivity : AppCompatActivity(), View.OnClickListener {



    lateinit var shared : SharedPreferences
    lateinit var routineOwnerEmail : String
    var userEmail : String = ""
    var routineId:Int = 0
    var userId : Int = 0
    private lateinit var networkConnection: NetworkConnection
    var isRoutineFavorite : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_routine_details)

        routineId = intent.extras!!.getInt("routineId")?:0
        routineOwnerEmail = intent.extras!!.getString("routineOwnerEmail")?:""


        shared = getSharedPreferences("login", Context.MODE_PRIVATE)
        userEmail = shared.getString("userEmail", "error")?:"null"
        userId = shared.getInt("id_usuario", 0);


        networkConnection=  NetworkConnection(applicationContext)

        networkConnection.observe(this, androidx.lifecycle.Observer {isOnline->

            if(isOnline){
                isRoutineAddedByUser()
                btnFavorites.visibility=View.VISIBLE
                ApiManager.getRoutineById(routineId, shapeableImageView, routineTitle, recyclerView, GridLayoutManager(applicationContext, 2),progressBar, progressBar2)

            }else{
                btnFavorites.visibility=View.GONE
                DatabaseInfo.db.getRoutineById(routineId, shapeableImageView, routineTitle, recyclerView, GridLayoutManager(applicationContext, 2),progressBar, progressBar2)
            }
        })





//        recyclerView.apply {
//            layoutManager = GridLayoutManager(applicationContext, 2)
//            adapter = RecyclerAdapterExercises()
//
//        }


        btnFavorites.setOnClickListener(this)
        shapeableImageView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
       when(v!!.id){

            R.id.btnFavorites ->{

                if(networkConnection.isOnline()){
                    if(isRoutineFavorite){
                        toggleFavoriteRoutine("removeFavoriteRoutine")
                    } else {
                        toggleFavoriteRoutine("addFavoriteRoutine")
                    }
                }else{
                    try{
                        Toast.makeText(this,getString(R.string.check_internet_conection),Toast.LENGTH_SHORT).show()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

            }
           R.id.shapeableImageView ->{

//               Check if the user logged in is the same then show the user activity otherwise show the public user activity
               if(networkConnection.isOnline()) {
                   if (userEmail != routineOwnerEmail) {
                       val userPublicActivity = Intent(this, PublicUserActivity::class.java)
                       userPublicActivity.putExtra("userEmail", routineOwnerEmail)
                       startActivity(userPublicActivity)
//                   finish()
                   } else {
                       val userIntent = Intent(this, UserActivity::class.java)

                       startActivity(userIntent)
                   }
               }else{
                   try{
                       //TODO arreglar bug
                       Toast.makeText(v.context,"Hola",Toast.LENGTH_SHORT).show()
                   }catch (e:Exception){
                       e.printStackTrace()
                   }

               }
           }


       }
    }

    private fun isRoutineAddedByUser(){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)

        val result: Call<Boolean> = service.isRoutineAddedByUser(routineId, userEmail)

        result.enqueue(object: Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(this@RoutineDetailsActivity, "Ha ocurrido un error con la conexion", Toast.LENGTH_LONG)
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val result = response.body()?:false

                if(result){
                    isRoutineFavorite = true;
                    btnFavorites.setImageResource(R.drawable.ic_favorite_24)

                } else {
                    btnFavorites.setImageResource(R.drawable.ic_favorite_border_24)
                }


            }
        })

    }

    private fun toggleFavoriteRoutine(context: String){
        val data = FavoriteRoutineData(
                    context,
                    routineId,
                    userId
        )
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)

        val result: Call<Boolean> = service.toggleFavoriteRoutine(data)

        result.enqueue(object: Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(this@RoutineDetailsActivity, "Ha ocurrido un error con la conexion", Toast.LENGTH_LONG)
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val result = response.body()?:false

                if(result){
                    if(context == "addFavoriteRoutine"){
//                        Toast.makeText(this@RoutineDetailsActivity, "Has agregado esta rutina a favortisos", Toast.LENGTH_SHORT).show()
                        isRoutineFavorite=true

                        btnFavorites.setAnimation(R.raw.hearthanimation4)
                        btnFavorites.addAnimatorListener(object : Animator.AnimatorListener{
                            override fun onAnimationStart(p0: Animator?) {

                            }

                            override fun onAnimationEnd(p0: Animator?) {
                                btnFavorites.setImageResource(R.drawable.ic_favorite_24)
                            }

                            override fun onAnimationCancel(p0: Animator?) {

                            }

                            override fun onAnimationRepeat(p0: Animator?) {

                            }


                        })
                        btnFavorites.playAnimation()

                       //
                    } else if(context == "removeFavoriteRoutine"){
                        DatabaseInfo.db.deleteFavoriteRoutineByIdAndEmail(routineId,userEmail)

                        isRoutineFavorite=false
//                        Toast.makeText(this@RoutineDetailsActivity, "La rutina ha sido removida de favoritos", Toast.LENGTH_SHORT).show()
                        btnFavorites.setImageResource(R.drawable.ic_favorite_border_24)
                    }
                }


            }
        })



    }
}