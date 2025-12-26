package com.mynt.quickstart

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform