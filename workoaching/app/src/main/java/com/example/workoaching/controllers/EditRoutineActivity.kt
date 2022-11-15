package com.example.workoaching.controllers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoaching.R
import com.example.workoaching.models.Routine
import com.example.workoaching.recycleradapter.RecyclerAdapterEditExercise
import com.example.workoaching.utils.ApiManager
import kotlinx.android.synthetic.main.activity_edit_routine.*

class EditRoutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_routine)
        supportActionBar!!.hide()
        var routineId : Int;
        routineId = intent.extras!!.getInt("routineId")?:0


       ApiManager.getRoutineExercisesById(routineId,routineName,editRoutineRecyclerView,LinearLayoutManager(this))


        applyChanges.setOnClickListener{



            val exercises=RecyclerAdapterEditExercise.getExercisesList()
            val routine = Routine(Id_Rutina = routineId,Nombre = routineName.text.toString(),Ejercicios = exercises)

            ApiManager.updateRoutine(routine,{success ->
                Toast.makeText(this,R.string.routine_updated,Toast.LENGTH_SHORT).show()
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                RecyclerAdapterEditExercise.clearList()
                finish()
            },{failure->


            })

        }

    }
}