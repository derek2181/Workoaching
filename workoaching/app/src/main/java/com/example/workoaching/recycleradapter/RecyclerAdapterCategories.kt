package com.example.workoaching.recycleradapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.R
import com.example.workoaching.controllers.CategoryActivity
import com.example.workoaching.models.Category
import com.example.workoaching.utils.CategoriesImagesLinker


class RecyclerAdapterCategories(var categories:List<Category>): RecyclerView.Adapter<RecyclerAdapterCategories.ViewHolder>(){

    private var titles = arrayOf("Primer Categoria", "Segunda Categoria", "Tercer Categoria", "Cuarta Categoria", "Quinta Categoria" )
    private val images = CategoriesImagesLinker.getCategiresImageArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.category_card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = categories[position].Nombre
        holder.itemImage.setImageResource(images[categories[position].Id_Categoria!! - 1])

    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)

            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val intent = Intent(itemView.context, CategoryActivity::class.java)
                    intent.putExtra("categoryName", categories[position].Nombre)
                    intent.putExtra("categoryId", categories[position].Id_Categoria)
                startActivity(itemView.context, intent, null)
            }
        }

    }


}