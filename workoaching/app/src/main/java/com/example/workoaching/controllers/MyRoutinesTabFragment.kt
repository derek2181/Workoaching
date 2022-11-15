package com.example.workoaching.controllers

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.workoaching.DAO.DatabaseInfo
import com.example.workoaching.R
import com.example.workoaching.recycleradapter.RecyclerAdapter
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.NetworkConnection
import com.example.workoaching.utils.SQLiteHelper

import kotlinx.android.synthetic.main.fragment_my_routines_tab.*
import kotlinx.android.synthetic.main.routine_card_layout.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyRoutinesTabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyRoutinesTabFragment(var ownRoutines:Boolean) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var shared : SharedPreferences
    private lateinit var networkConnection: NetworkConnection
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_routines_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shared = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
        val userEmail = shared.getString("userEmail", "error")?:"null"
        networkConnection= activity?.let { NetworkConnection(it.applicationContext) }!!

        val textNoRoutines=view.findViewById<TextView>(R.id.txtNoRoutines)
        val recyclerRoutines=view.findViewById<RecyclerView>(R.id.routinesRecyclerView)
        val progressBar=view.findViewById<ProgressBar>(R.id.progressBarMyRoutines)
        val routinesTab=view.findViewById<LinearLayout>(R.id.routinesTabContainer)
        val offlineContainer=view.findViewById<LinearLayout>(R.id.offlineError)

        networkConnection.observe(requireActivity(), Observer{ isConnected->

            if(isConnected){

                routinesTab.visibility=View.VISIBLE
                offlineContainer.visibility=View.GONE
                if(ownRoutines){
                    ApiManager.getUserRoutines(userEmail, true, recyclerRoutines, GridLayoutManager(activity, 2), progressBar, textNoRoutines)

                } else {
                    ApiManager.getUserFavoriteRoutines(userEmail, recyclerRoutines, GridLayoutManager(activity, 2), progressBar, textNoRoutines)

                }
            }else{

                if(ownRoutines){

                    routinesTab.visibility=View.GONE
                    offlineContainer.visibility=View.VISIBLE
                } else {
                    DatabaseInfo.db.getFavoriteRoutines(recyclerRoutines, GridLayoutManager(activity, 2), progressBar,userEmail)
                }

            }
        })



    }

}