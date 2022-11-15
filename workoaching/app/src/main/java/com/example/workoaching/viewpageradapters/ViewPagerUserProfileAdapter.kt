package com.example.workoaching.viewpageradapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.workoaching.controllers.UserProfileFields
import com.example.workoaching.controllers.UserProfileImages

class ViewPagerUserProfileAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment){
    var userImagesFragment : UserProfileImages
    var userProfileFields: UserProfileFields
    override fun getItemCount(): Int = 2
    init {
        userProfileFields = UserProfileFields()
        userImagesFragment = UserProfileImages()
    }
    override fun createFragment(position: Int): Fragment {


        when(position){

            0->{return userImagesFragment}
            1->{return userProfileFields}
        }


        return userProfileFields

    }

}