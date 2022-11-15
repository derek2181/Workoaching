package com.example.workoaching.controllers

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.workoaching.R
import com.example.workoaching.models.Routine
import com.example.workoaching.recycleradapter.RecyclerAdapter
import com.example.workoaching.utils.ApiManager
import com.example.workoaching.utils.NetworkConnection
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var networkConnection: NetworkConnection
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    lateinit var shared : SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        recyclerView.apply {
////            layoutManager = GridLayoutManager(activity, 2)
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
////            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
//            adapter = RecyclerAdapterExercises()
//
//
//        }
           networkConnection= activity?.let { NetworkConnection(it.applicationContext) }!!

        val layoutManager = FlexboxLayoutManager(activity, FlexDirection.ROW, FlexWrap.NOWRAP)



        val recycler=view.findViewById<RecyclerView>(R.id.recyclerViewPopular)
        val recyclerNewRoutines=view.findViewById<RecyclerView>(R.id.recyclerViewNuevasRutinas)
        val homeCont=view.findViewById<LinearLayout>(R.id.homeContainer)
        val homeOfflineCont=view.findViewById<LinearLayout>(R.id.homeOfflineContainer)
        val firstProgressBar=view.findViewById<ProgressBar>(R.id.progressBar)
        val secondProgressBar2=view.findViewById<ProgressBar>(R.id.progressBar2)
        shared = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)

        val userEmail = shared.getString("userEmail", "error")?:"null"


        networkConnection.observe(requireActivity(),Observer{ isConnected->


           // val popularRecycler=view.findViewById<RecyclerViewr>(R.id.recyclerViewPopular)

            homeCont.visibility=View.VISIBLE
            homeOfflineCont.visibility=View.GONE
            if(isConnected){


                ApiManager.getPopularRoutines(userEmail, recycler,FlexboxLayoutManager(activity, FlexDirection.ROW, FlexWrap.NOWRAP), firstProgressBar)
                ApiManager.getRecentRoutines(userEmail, recyclerNewRoutines,FlexboxLayoutManager(activity, FlexDirection.ROW, FlexWrap.NOWRAP), secondProgressBar2)

            }else{
                homeCont.visibility=View.GONE
                homeOfflineCont.visibility=View.VISIBLE
            }
        })


//        recyclerViewNuevasRutinas.apply {
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//            adapter = RecyclerAdapterExercises()
//        }








        if(networkConnection.isOnline()){


        ApiManager.getPopularRoutines(userEmail, recycler,FlexboxLayoutManager(activity, FlexDirection.ROW, FlexWrap.NOWRAP), firstProgressBar)
        ApiManager.getRecentRoutines(userEmail, recyclerNewRoutines,FlexboxLayoutManager(activity, FlexDirection.ROW, FlexWrap.NOWRAP), secondProgressBar2)

        }else{
            homeContainer.visibility=View.GONE
            homeOfflineContainer.visibility=View.VISIBLE
        }


//        ApiManager.getRecentRoutines(userEmail, recyclerViewNuevasRutinas,LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false), progressBar2)
//        layoutManager = LinearLayoutManager(activity)
//        recyclerView!!.layoutManager = layoutManager
//        adapter = RecyclerAdapter()
//        recyclerView!!.adapter = adapter
    }


}