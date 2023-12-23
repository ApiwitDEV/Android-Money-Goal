package com.example.data.datasource.remote.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

class Connectivity(private val context: Context) {

    var state: ConnectivityState? = null

    enum class ConnectivityState {
        Available, Losing
    }

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            state = ConnectivityState.Available
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            state = ConnectivityState.Losing
        }
    }

    fun requestNetwork() {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    fun isAvailable(): Boolean {
        return connectivityManager.activeNetworkInfo?.isAvailable?: false
    }

}