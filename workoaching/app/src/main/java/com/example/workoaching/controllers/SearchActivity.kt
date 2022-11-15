package com.example.workoaching.controllers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.workoaching.R
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_search)
        ApiManager.getAllCategories(recyclerView, GridLayoutManager(applicationContext, 2), progressBarSearchCategories)
       // recyclerView.apply {
//            layoutManager = GridLayoutManager(applicationContext, 2)
//            adapter = RecyclerAdapterCategories()
//        }


        val networkConnection= NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer{ isConnected->

            if(!isConnected){
                val intent= Intent(this,MainActivity::class.java)

                startActivity(intent)
            }
        })
    }
}