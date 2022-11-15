package com.example.workoaching.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestEngine {

    companion object{
        fun getRestEngine():Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2/apiworkoaching/")
             // .baseUrl("http://workoaching2.proyectoslmad.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return  retrofit
        }
    }
}