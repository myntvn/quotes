package com.mynt.quickstart

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val id: Int,
    val content: String,
    val author: String,
    val details: String
)
