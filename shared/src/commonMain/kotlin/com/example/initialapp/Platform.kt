package com.example.initialapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform