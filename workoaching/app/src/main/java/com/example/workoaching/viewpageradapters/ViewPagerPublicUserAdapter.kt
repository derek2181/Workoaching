package com.example.workoaching.viewpageradapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.workoaching.controllers.PublicUserImages
import com.example.workoaching.controllers.PublicUserRoutines

class ViewPagerPublicUserAdapter(fragment : FragmentActivity, var userEmail : String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = PublicUserRoutines(userEmail)
        val fragmentPictures = PublicUserImages(userEmail)
        when(position){
            0->{return fragment}
            1->{return fragmentPictures}

        }
        return fragment
    }
}