package com.example.workoaching.controllers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.workoaching.viewpageradapters.MyRoutinesViewPagerAdapter
import com.example.workoaching.R
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyRoutinesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyRoutinesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var myFragment: View
    lateinit var viewPagers: ViewPager
    lateinit var tabLayouts: TabLayout

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
        val view = inflater.inflate(R.layout.fragment_my_routines, container, false)
        setUpViewPager(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: android.os.Bundle?){
        super.onActivityCreated(savedInstanceState)


//        tabLayouts.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//        })



    }
    private fun setUpViewPager(v: View) {
        viewPagers = v.findViewById<ViewPager>(R.id.pager)
        viewPagers.isSaveEnabled = false
        tabLayouts = v.findViewById<TabLayout>(R.id.tab_layout)

        var adapter = MyRoutinesViewPagerAdapter(childFragmentManager)

        adapter.addFragment(MyRoutinesTabFragment(true), getString(R.string.my_routines_tabs_my_routines))
        adapter.addFragment(MyRoutinesTabFragment(false), getString(R.string.my_routines_tabs_favorites))

        viewPagers!!.adapter = adapter
        tabLayouts!!.setupWithViewPager(viewPagers)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyRoutinesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyRoutinesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}