package com.overshoot.moneygoal.flutterinteractor

import android.app.Activity

interface FlutterInterface {

    val flutterEngineId: String

    fun executeDart()

    fun executeDartAndCache()

    fun startFlutterActivity(activity: Activity)

    fun clearCache()

}