package com.example.workoaching.recycleradapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.R
import com.example.workoaching.api.RestEngine
import com.example.workoaching.api.Service
import com.example.workoaching.controllers.EditRoutineActivity
import com.example.workoaching.controllers.RoutineDetailsActivity
import com.example.workoaching.models.Routine
import com.example.workoaching.models.RoutineData
import com.example.workoaching.utils.CategoriesImagesLinker
import com.example.workoaching.utils.NetworkConnection
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.routine_card_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecyclerAdapter(var routines:MutableList<Routine>, var userLoggedInEmail : String?): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() , Filterable{
    private var context: Context? = null

    private val images = CategoriesImagesLinker.getCategiresImageArray()
    private lateinit var networkConnection: NetworkConnection


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.routine_card_layout, parent, false)

        networkConnection= NetworkConnection(v.context)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = routines[position].Nombre
        holder.itemAuthor.text = "${ holder.itemView.context.getString(R.string.made_by)}: ${routines[position].Autor}"

        val routineCategoriesRaw = routines[position].Categorias?: ""

        if(userLoggedInEmail != null){
            if(userLoggedInEmail != routines[position].Email){
                holder.btnMoreOptions.visibility = View.GONE
            }
        } else{
            holder.btnMoreOptions.visibility = View.GONE
        }
        if(networkConnection.isOnline()){
            holder.itemView.downloadableIcon.visibility=View.GONE
        }else{
            holder.itemView.downloadableIcon.visibility=View.VISIBLE
        }


        val routineCategories = TextUtils.split(routineCategoriesRaw, ",")
        holder.itemGridImages.removeAllViews()

        for (category in routineCategories){
            val categoryNumber = category.toString().toInt()

            val imageView:ImageView = ImageView(context)

            imageView.setImageResource(images[categoryNumber-1])
//            imageView.layoutParams.width = 40
//            imageView.layoutParams.height = 40
//            var layoutParams = ViewGroup.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT)
            var layoutParams = ViewGroup.LayoutParams(8, 8)

//            imageView.layoutParams = layoutParams



            holder.itemGridImages.addView(imageView)

        }

        holder.routineId = routines[position].Id_Rutina
    }

    override fun getItemCount(): Int {
        return routines!!.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var routineId: Int?

            var itemTitle: TextView
            var itemAuthor: TextView
            var itemGridImages: FlexboxLayout
            var btnMoreOptions: ImageView

            init {


                if(itemView.layoutParams is FlexboxLayoutManager.LayoutParams ){
                    val flexboxLp = itemView.layoutParams as FlexboxLayoutManager.LayoutParams
                    flexboxLp.flexShrink = 0.0f
                    flexboxLp.alignSelf = AlignItems.CENTER
                }

//                itemImage = itemView.findViewById(R.id.item_image)
                itemTitle = itemView.findViewById(R.id.item_title)
//                itemImage2 = itemView.findViewById(R.id.item_image2)
                itemAuthor = itemView.findViewById(R.id.item_author)
                itemGridImages = itemView.findViewById(R.id.grid_images)
                routineId = null
                btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions)



                btnMoreOptions.setOnClickListener{popupMenu(it)}

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, RoutineDetailsActivity::class.java)
                        intent.putExtra("routineId", routines[adapterPosition].Id_Rutina)
                        intent.putExtra("routineOwnerEmail", routines[adapterPosition].Email)
                    startActivity(itemView.context, intent, null)

//                    (itemView.context as Activity).finish()

                }
            }

        private fun popupMenu(v:View) {
            val popupMenus = PopupMenu(context, v)
            popupMenus.inflate(R.menu.show_menu)

            val test = popupMenus.menu
//            Check if the user is the owner of the routine if it is then show the whole options otherwise show just add to favorites
//            test.findItem(R.id.btnEditRoutine).setVisible(false)
//            test.findItem(R.id.btnDeleteRoutine).setEnabled(false)


            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.btnEditRoutine -> {

                        val editActivityIntent=Intent(itemView.context,EditRoutineActivity::class.java)
                        editActivityIntent.putExtra("routineId", routines[adapterPosition].Id_Rutina)
                        itemView.context.startActivity(editActivityIntent)

                        true


                    }
                    R.id.btnDeleteRoutine -> {

                        AlertDialog.Builder(context)
                            .setTitle(context!!.getString(R.string.routine_delete))
                            .setIcon(R.drawable.ic_warning_24)
                            .setMessage(context!!.getString(R.string.routine_delete_confirm))
                            .setPositiveButton(context!!.getString(R.string.yes)){
                                dialog,_->
                                DeleteRoutine(dialog, adapterPosition)
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


    private fun DeleteRoutine(dialog: DialogInterface, position: Int) {
        val routineData = RoutineData(
                    "delete",
                    routines[position].Id_Rutina
        )


        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Boolean> = service.deleteRoutineById(routineData)

        result.enqueue(object : Callback<Boolean>{
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(context, context!!.getString(R.string.routine_delete_failure), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val result = response.body()?:false
                if(result){
                    routines.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(context, context!!.getString(R.string.routine_delete_sucess), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context!!.getString(R.string.routine_delete_failure), Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }


}