package com.example.workoaching.utils

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData


class NetworkConnection(private val context  : Context):LiveData<Boolean>() {





    fun isOnline(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
    private var connectivityManager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun  lollipopNetworkRequest(){
        val requestBuilder= NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

        connectivityManager.registerNetworkCallback(
            requestBuilder.build(),
            connectivityManagerCallback()
        )

    }

    override fun onActive() {
        super.onActive()
        updateConection()
        when{
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.N->{
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback())
            }
            Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP->{
                lollipopNetworkRequest()
            }
            else->{
                context.registerReceiver(
                    networkReceiver,
                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                )
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }else{
            context.unregisterReceiver(networkReceiver)
        }
    }
    private fun connectivityManagerCallback():ConnectivityManager.NetworkCallback{
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {


            networkCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onLost(network: Network) {
                    super.onLost(network)

                    postValue(false)
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)

                    postValue(true)
                }


            }
            return networkCallback
        }
        else {
           throw IllegalAccessError("Error")
        }
        }

    private val networkReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConection()
        }
    }


    private fun updateConection(){
        val activeNetwork: NetworkInfo?=connectivityManager.activeNetworkInfo

        postValue(activeNetwork?.isConnected==true)

    }






    }