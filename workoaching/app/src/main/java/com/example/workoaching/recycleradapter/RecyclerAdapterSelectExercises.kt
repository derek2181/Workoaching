package com.example.workoaching.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.R
import com.example.workoaching.models.Exercise
import com.example.workoaching.utils.ImageUtilities
import kotlinx.android.synthetic.main.select_exercise_card_layout.view.*
import java.util.*

import android.view.MotionEvent
import com.example.workoaching.controllers.CreationFragment
import kotlinx.android.synthetic.main.fragment_creation.view.*


class RecyclerAdapterSelectExercises(
    val exercises: MutableList<Exercise>,
) : RecyclerView.Adapter<RecyclerAdapterSelectExercises.ExercisesViewHolder>(){
    class ExercisesViewHolder(val view: View): RecyclerView.ViewHolder(view)

    var x2:Float=0.0f
    var x1:Float=0.0f

    companion object{
        private val selectedExercises=HashMap<String,Exercise>()
        var routineName : String = "";

        fun getSelectedExercises() : MutableList<Exercise> {
            val exerciseList= mutableListOf<Exercise>()

            for(exercise in selectedExercises){

                exerciseList.add(exercise.value)
            }

            return exerciseList

        }
        fun clearList(){
            selectedExercises.clear()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        return ExercisesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.select_exercise_card_layout,parent,false)
        );
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {

        val exercise : Exercise =exercises[position]
        holder.view.exerciseName.text=exercise.Nombre
       // holder.view.selectedExerciseSuccess.visibility=View.GONE
        var byteArray:ByteArray? = null
        byteArray = Base64.getDecoder().decode(exercise.Imagen)

        if(byteArray != null){

            holder.view.exerciseImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
        }
        if(!selectedExercises.containsKey(exercise.Id_ejercicio.toString())){
            holder.view.selectedExerciseSuccess.visibility=View.GONE
            holder.view.selectCardBorder.setBackgroundResource(R.drawable.unselected_border)

        }else{
            holder.view.selectedExerciseSuccess.visibility=View.VISIBLE
            holder.view.selectCardBorder.setBackgroundResource(R.drawable.selected_border)
        }


        holder.view.setOnClickListener {

            if (selectedExercises.containsKey(exercise.Id_ejercicio.toString())) {
               holder.view.selectedExerciseSuccess.visibility = View.GONE
                holder.view.selectCardBorder.setBackgroundResource(R.drawable.unselected_border)
                selectedExercises.remove(exercise.Id_ejercicio.toString())


           } else {
               holder.view.selectedExerciseSuccess.visibility = View.VISIBLE
                holder.view.selectCardBorder.setBackgroundResource(R.drawable.selected_border)
                selectedExercises.put(exercise.Id_ejercicio.toString(), exercise)


                // holder.view.exerciseImage.setColorFilter(R.color.greeb_selection, PorterDuff.Mode.SRC_ATOP)

           }

        }
    }

    override fun getItemCount()= exercises.size


}