package com.example.workoaching.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.R
import com.example.workoaching.models.Exercise
import com.example.workoaching.utils.ImageUtilities

import kotlinx.android.synthetic.main.exercise_series_card_layout.view.*

import java.util.*
import kotlin.collections.ArrayList


class RecyclerAdapterEditExercise(
    val exercises   : List<Exercise>
) : RecyclerView.Adapter<RecyclerAdapterEditExercise.ExercisesViewHolder>() {
    class ExercisesViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        return ExercisesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.exercise_series_card_layout, parent, false)
        );
    }

    companion object {
        var exerciseList = mutableListOf<Exercise>()


    fun setList(exercises: List<Exercise>){
        exerciseList= exercises as MutableList<Exercise>
    }

        fun clearList(){
            exerciseList.clear()
        }
    fun getExercisesList(): MutableList<Exercise> {
        val listOfExercises= mutableListOf<Exercise>()
        for (exercise in exerciseList) {

            listOfExercises.add(exercise)

        }

        return listOfExercises
    }
}


    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {

        val exercise : Exercise = getExercisesList()[position]
        holder.view.seriesNumber.maxValue=10
        holder.view.seriesNumber.minValue=1
        holder.view.repetitionsNumber.maxValue=32
        holder.view.repetitionsNumber.minValue=1

        var byteArray:ByteArray? = null
        byteArray = Base64.getDecoder().decode(exercise.Imagen)

        if(byteArray != null){

            holder.view.exerciseImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
        }

        holder.view.exerciseName.text=exercise.Nombre

        holder.view.seriesNumber.value=exercise.Series!!
        holder.view.repetitionsNumber.value=exercise.Repeticiones!!



        holder.view.seriesNumber.setOnValueChangedListener { _, oldVal, newVal ->
            getExercisesList()[position].Series = newVal
            //  Toast.makeText(holder.view.context,newVal.toString(),Toast.LENGTH_SHORT).show()
        }

        holder.view.repetitionsNumber.setOnValueChangedListener { _, oldVal, newVal ->
            getExercisesList()[position].Repeticiones = newVal
            //Toast.makeText(holder.view.context,newVal.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount()=exercises.size


}