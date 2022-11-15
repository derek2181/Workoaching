package com.example.workoaching.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoaching.R
import com.example.workoaching.models.Routine
import com.example.workoaching.recycleradapter.RecyclerAdapterRoutineExercises
import com.example.workoaching.recycleradapter.RecyclerAdapterSelectExercises
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.NetworkConnection
import com.example.workoaching.utils.SQLiteHelper
import kotlinx.android.synthetic.main.activity_create_routine.*


class CreateRoutineActivity : AppCompatActivity() {
    private lateinit var routineName : String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)
        supportActionBar!!.hide()


        routineName = intent.extras!!.getString("routineName")?:""

        adjustExercisesRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = RecyclerAdapterRoutineExercises()
        }

        val networkConnection= NetworkConnection(applicationContext)
       // networkConnection.observe(this, Observer{ isConnected->

        //    if(isConnected){
       //         Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show()
       //     }else{
       //         Toast.makeText(this,"Not connected",Toast.LENGTH_SHORT).show()
       //     }
      //  })



        createRoutineButton.setOnClickListener{
            val  shared = getSharedPreferences("login", Context.MODE_PRIVATE)
            val idUsuario : Int? = shared.getInt("id_usuario", 0)?:"null".toInt()

            val ejercicios= RecyclerAdapterRoutineExercises().getExercisesList()
            RecyclerAdapterSelectExercises.routineName=""
            RecyclerAdapterSelectExercises.clearList()


            val routine= Routine(Ejercicios=ejercicios,Id_Usuario = idUsuario,Nombre =routineName )



        if(networkConnection.isOnline()){


        ApiManager.addRoutine(routine,{response ->


            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this,R.string.routine_added,Toast.LENGTH_SHORT).show()

            //TODO resetear la lista en la que tengo los ejercicios seleccionados y hacer otra lista estatica en la que se actualice el numero de los ejercicios y las repeticiones

        },{T->


            Toast.makeText(this,"Fallo",Toast.LENGTH_SHORT).show()
        })

        }else{
            Routine.offlineRoutinesCreated.add(routine)
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this,R.string.routine_added_offline,Toast.LENGTH_SHORT).show()
        }
        }


    }


}