package com.overshoot.data.datasource.remote.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow

class Connectivity(context: Context) {

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    var state = MutableStateFlow(ConnectivityState.Losing)

    enum class ConnectivityState {
        Available, Losing
    }

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            state.value = ConnectivityState.Available
        }

        // Network capabilities have changed for the network
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
//            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            Log.i("connectivity", networkCapabilities.signalStrength.toString())
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            state.value = ConnectivityState.Losing
        }
    }

    fun requestNetwork() {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    fun isAvailable(): Boolean {
        return state.value == ConnectivityState.Available
    }

}