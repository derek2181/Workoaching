package com.example.workoaching.recycleradapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.R
import com.example.workoaching.models.Image
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.ImageUtilities
import kotlinx.android.synthetic.main.activity_user.*
import java.util.*
import kotlin.collections.ArrayList


class RecyclerAdapterUserImages(var images:ArrayList<Image>, var allowRemove:Boolean): RecyclerView.Adapter<RecyclerAdapterUserImages.ViewHolder>(){
    private var context: Context? = null
    private var titles = arrayOf("Primer rutina", "Segunda rutina", "Tercer Rutina", "Cuarta Rutina", "Quinta Rutina" , "Quinta Rutina" , "Quinta Rutina")
    private var authors = arrayOf("Derek Cortes", "Derek Cortes", "Derek Cortes", "Derek Cortes", "Derek Cortes", "Derek Cortes", "Derek Cortes")
//    private val images = intArrayOf(R.drawable.card_picture, R.drawable.card_picture, R.drawable.card_picture, R.drawable.card_picture, R.drawable.card_picture, R.drawable.card_picture, R.drawable.card_picture)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_image_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        holder.itemImage.setImageResource(R.drawable.profile_picture)
        var byteArray:ByteArray? = null
        if(!images[position].imagen.isNullOrEmpty()){
            byteArray = Base64.getDecoder().decode(images[position].imagen)
            if(byteArray != null){
//                shapeableImageView.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
                holder.itemImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView


        init {
            itemImage = itemView.findViewById(R.id.image_view_item)

            if(allowRemove) {
                itemView.setOnLongClickListener {
                    popupMenu(it)
                    return@setOnLongClickListener (true)
                }
            }

        }

        private fun popupMenu(view:View){
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.user_image_menu)

            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.btnDeleteImage -> {

                        AlertDialog.Builder(context)
                            .setTitle(context!!.getString(R.string.image_delete))
                            .setIcon(R.drawable.ic_warning_24)
                            .setMessage(context!!.getString(R.string.image_delete_confirm))
                            .setPositiveButton(context!!.getString(R.string.yes)){
                                    dialog,_->
                                deleteImage(adapterPosition)
                                dialog.dismiss()
                            }
                            .setNegativeButton(context!!.getString(R.string.no)){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }
            }

            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(menu, true)
        }

    }

    private fun deleteImage(position: Int){
        ApiManager.deleteUserImage(images[position].id_imagen!!, {response->
            val result = response.body()?:false
            if(result){
                images.removeAt(position)
                notifyDataSetChanged()
                Toast.makeText(context, context!!.getString(R.string.image_delete_success), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context!!.getString(R.string.image_delete_failure), Toast.LENGTH_SHORT).show()
            }

        }, {t->
            Toast.makeText(context, context!!.getString(R.string.image_delete_failure), Toast.LENGTH_SHORT).show()

        })

    }


}