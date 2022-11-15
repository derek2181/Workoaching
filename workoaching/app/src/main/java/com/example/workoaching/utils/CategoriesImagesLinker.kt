package com.example.workoaching.utils

import com.example.workoaching.R

class CategoriesImagesLinker {
    companion object {
        fun getCategiresImageArray():IntArray{

            val categoriesImages = intArrayOf(R.drawable.pecho_category,
                                                R.drawable.hombro_category,
                                            R.drawable.espalda_cateogry,
                                            R.drawable.brazo_category,
                                            R.drawable.abdomen_category)

            return categoriesImages
        }

    }
}