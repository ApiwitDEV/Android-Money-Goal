package com.overshoot.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform