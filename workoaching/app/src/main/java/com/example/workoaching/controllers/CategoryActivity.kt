package com.example.workoaching.controllers

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.DAO.DatabaseInfo
import com.example.workoaching.R
import com.example.workoaching.models.Routine
import com.example.workoaching.recycleradapter.RecyclerAdapter
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.CategoriesImagesLinker
import com.example.workoaching.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_edit_routine.view.*
import kotlinx.android.synthetic.main.activity_search.recyclerView
import kotlinx.android.synthetic.main.activity_search.txtHeader

class CategoryActivity  : AppCompatActivity() {
    private var routines:List<Routine>? = null
    private val categoryImages = CategoriesImagesLinker.getCategiresImageArray()
    lateinit var shared : SharedPreferences
    private lateinit var networkConnection: NetworkConnection
    companion object{
         var filterList = mutableListOf<Routine>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_category)

        shared = getSharedPreferences("login", Context.MODE_PRIVATE)
        val categoryName = intent.extras!!.getString("categoryName")?: "No se pudo"
        val categoryId = intent.extras!!.getInt("categoryId")?: 0
        val recycler=findViewById<RecyclerView>(R.id.recyclerViewRoutineCategories)
        val progress=findViewById<ProgressBar>(R.id.progressBar)
        val radioAz=findViewById<CheckBox>(R.id.orderedAzRadio)
        val radioZa=findViewById<CheckBox>(R.id.orderedZaRadio)


        val userEmail = shared.getString("userEmail", "error")?:"null"
        radioAz.setOnClickListener{
            radioZa.isChecked=false

            if(radioAz.isChecked){
                val queryText=searchText.text.toString()

                var filteredList = mutableListOf<Routine>()

                if(!queryText.isNullOrEmpty()){
                    for(routine in filterList!!) {
                        val str1 = routine.Nombre?.lowercase()!!
                        val str2 = queryText.lowercase()
                        if (routine.Nombre?.lowercase()!!.startsWith(queryText.lowercase())) {
                            filteredList.add(routine)
                        }
                    }
                }else{
                    filteredList= filterList
                }

                val list=filteredList.sortedBy { it.Nombre } as MutableList<Routine>
                recycler.layoutManager=GridLayoutManager(applicationContext, 2)
                recycler.adapter=RecyclerAdapter(list,userEmail)
            }else{
                val queryText=searchText.text.toString()

                var filteredList = mutableListOf<Routine>()

                if(!queryText.isNullOrEmpty()){
                    for(routine in filterList!!) {
                        val str1 = routine.Nombre?.lowercase()!!
                        val str2 = queryText.lowercase()
                        if (routine.Nombre?.lowercase()!!.startsWith(queryText.lowercase())) {
                            filteredList.add(routine)
                        }
                    }
                }else{
                    filteredList= filterList
                }
                recycler.layoutManager=GridLayoutManager(applicationContext, 2)
                recycler.adapter=RecyclerAdapter(filteredList,userEmail)
            }







        }
        radioZa.setOnClickListener{
            radioAz.isChecked=false


            if(radioZa.isChecked){
                val queryText=searchText.text.toString()

                var filteredList = mutableListOf<Routine>()

                if(!queryText.isNullOrEmpty()){
                    for(routine in filterList!!) {
                        val str1 = routine.Nombre?.lowercase()!!
                        val str2 = queryText.lowercase()
                        if (routine.Nombre?.lowercase()!!.startsWith(queryText.lowercase())) {
                            filteredList.add(routine)
                        }
                    }
                }else{
                    filteredList= filterList
                }

                val list=filteredList.sortedByDescending { it.Nombre } as MutableList<Routine>
                recycler.layoutManager=GridLayoutManager(applicationContext, 2)
                recycler.adapter=RecyclerAdapter(list,userEmail)
            }else{
                val queryText=searchText.text.toString()

                var filteredList = mutableListOf<Routine>()

                if(!queryText.isNullOrEmpty()){
                    for(routine in filterList!!) {
                        val str1 = routine.Nombre?.lowercase()!!
                        val str2 = queryText.lowercase()
                        if (routine.Nombre?.lowercase()!!.startsWith(queryText.lowercase())) {
                            filteredList.add(routine)
                        }
                    }
                }else{
                    filteredList= filterList
                }
                recycler.layoutManager=GridLayoutManager(applicationContext, 2)
                recycler.adapter=RecyclerAdapter(filteredList,userEmail)
            }





        }

        searchText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(searchQuery: CharSequence?, start: Int, before: Int, count: Int) {
                val constraint = searchQuery.toString()
                if(searchQuery.isNullOrEmpty()){
                    radioZa.isChecked=false
                    radioAz.isChecked=false
                    recycler.layoutManager=GridLayoutManager(applicationContext, 2)
                    recycler.adapter=RecyclerAdapter(filterList,userEmail)

                }else{
                    val filteredList = mutableListOf<Routine>()
                    for(routine in filterList!!) {
                        val str1 = routine.Nombre?.lowercase()!!
                        val str2 = constraint.lowercase()
                        if (routine.Nombre?.lowercase()!!.startsWith(constraint.lowercase())) {
                            filteredList.add(routine)
                        }
                    }
                    recycler.layoutManager=GridLayoutManager(applicationContext, 2)
                    recycler.adapter=RecyclerAdapter(filteredList,userEmail)

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        txtHeader.text = "Ejercicios de ${categoryName}"
        categoryImageView.setImageResource(categoryImages[categoryId-1])
        ApiManager.getCategoryRoutines(userEmail, categoryId,recycler,GridLayoutManager(applicationContext, 2),progressBar )





        val networkConnection= NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer{ isConnected->

            if(!isConnected){
            val intent= Intent(this,MainActivity::class.java)

                startActivity(intent)
            }
        })



    }

//    private fun getRecentRoutines(){
//        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
//
//        val result: Call<List<Routine>> = service.getAllRoutines()
//
//        result.enqueue(object: Callback<List<Routine>> {
//            override fun onFailure(call: Call<List<Routine>>, t: Throwable) {
//                val hol = "asdas"
//            }
//
//            override fun onResponse(call: Call<List<Routine>>, response: Response<List<Routine>>) {
//                routines = response.body()
//                recyclerView.apply {
//                    layoutManager = GridLayoutManager(applicationContext, 2)
//                    adapter = RecyclerAdapter(routines!!)
//                }
//            }
//        })
//
//    }
}