package com.overshoot.data.datasource.remote.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class InternetConnectivity(context: Context) {

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private val networkList = mutableListOf<Network>()

    val state = callbackFlow {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        trySend(
            if (isAvailable()) {
                InternetConnectivityState.Available
            }
            else {
                InternetConnectivityState.Losing
            }
        )

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                networkList += network
                trySend(InternetConnectivityState.Available)
            }

            // Network capabilities have changed for the network
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
            }

            // lost network connection
            override fun onLost(network: Network) {
                networkList -= network
                if (networkList.isEmpty()) {
                    trySend(InternetConnectivityState.Losing)
                }
                else {
                    trySend(InternetConnectivityState.Available)
                }
            }
        }
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    fun isAvailable(): Boolean {
        return connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)?:false
    }

    enum class InternetConnectivityState {
        Available, Losing
    }

}