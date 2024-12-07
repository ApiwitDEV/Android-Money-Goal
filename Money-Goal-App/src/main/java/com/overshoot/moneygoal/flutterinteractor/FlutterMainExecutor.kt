package com.overshoot.moneygoal.flutterinteractor

import android.app.Activity
import android.content.Context
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class FlutterMainExecutor(
    private val context: Context,
    override val flutterEngineId: String
): FlutterInterface {

    private var flutterEngine: FlutterEngine? = null
    private val flutterLoader = FlutterInjector.instance().flutterLoader()
    private val flutterEngineCache = FlutterEngineCache.getInstance()

    private val dartEntrypointFunctionName = "main"

    init {
        println()
    }

    override fun executeDart() {
        flutterEngine = FlutterEngine(context)
        if (!flutterLoader.initialized()) {
            throw AssertionError(
                "Dart Entry points can only be created once a FlutterEngine is created."
            )
        }
        val dartEntrypoint = DartExecutor
            .DartEntrypoint(
                flutterLoader.findAppBundlePath(),
                dartEntrypointFunctionName
            )
        flutterEngine?.dartExecutor?.executeDartEntrypoint(dartEntrypoint)
    }

    override fun executeDartAndCache() {
        executeDart()
        flutterEngineCache.put(flutterEngineId, flutterEngine)
    }

    override fun clearCache() {
        flutterEngine = null
        flutterEngineCache.remove(flutterEngineId)
    }

    override fun startFlutterActivity(activity: Activity) {
        activity.startActivity(
            FlutterActivity
                .withCachedEngine(flutterEngineId)
                .build(activity)
        )
    }

}