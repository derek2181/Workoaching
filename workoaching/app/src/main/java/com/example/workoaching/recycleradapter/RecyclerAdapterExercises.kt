package com.example.workoaching.recycleradapter

import android.annotation.SuppressLint
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.R
import com.example.workoaching.models.Exercise
import com.example.workoaching.utils.ImageUtilities
import java.util.*

class RecyclerAdapterExercises(var exercises: List<Exercise>?) : RecyclerView.Adapter<RecyclerAdapterExercises.ViewHolder>(){

    private val titles = arrayOf("Ejercicio 1", "Ejercicio 2", "Ejercicio 3")
    private val series = arrayOf(12, 15, 15)
    private val reps = arrayOf(10,20, 10)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.exercise_card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = exercises!![position].Nombre

        var byteArray:ByteArray? = null
        byteArray = Base64.getDecoder().decode(exercises!![position].Imagen)

        if(byteArray != null){

            holder.itemImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
        }



        holder.itemSeries.text ="${ holder.itemView.context.getString(R.string.series)}: ${exercises!![position].Series}"
        holder.itemReps.text = "${holder.itemView.context.getString(R.string.repetitions)}: ${exercises!![position].Repeticiones}"
    }

    override fun getItemCount(): Int {
        return exercises!!.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemTitle: TextView
        var itemImage: ImageView
        var itemSeries: TextView
        var itemReps: TextView

        init {
            itemTitle = itemView.findViewById(R.id.item_title)
            itemImage = itemView.findViewById(R.id.item_image)
            itemSeries = itemView.findViewById(R.id.item_series)
            itemReps = itemView.findViewById(R.id.item_reps)


        }

    }


}