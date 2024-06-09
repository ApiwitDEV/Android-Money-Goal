package com.overshoot.data.datasource.remote.network

data class NetworkStateModel(
    val internetConnectivityState: InternetConnectivity.InternetConnectivityState,
    val signalStrength: Int,
    val linkDownstreamBandwidthKbps: Int,
    val linkUpstreamBandwidthKbps: Int
)