package com.example.initialapp.feature.canvas.model

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val red: Float,
    val green: Float,
    val blue: Float,
) {
    init {
        require(red in 0f..1f) { "Red should be in range from 0 to 1" }
        require(green in 0f..1f) { "Green should be in range from 0 to 1" }
        require(blue in 0f..1f) { "Blue should be in range from 0 to 1" }
    }

    fun toHexString(): String =
        "${
            (red * 255).toInt().toString(16)
        }${
            (green * 255).toInt().toString(16)
        }${
            (blue * 255).toInt().toString(16)
        }"

    companion object {
        val Black = Color(0F, 0F, 0F)
        val White = Color(1F, 1F, 1F)
    }
}

fun String.toColor(): Color {
    val hexNum = this.toInt(16)
    return Color(
        hexNum.shr(16).and(0xFF) / 255F,
        hexNum.shr(8).and(0xFF) / 255F,
        hexNum.and(0xFF) / 255F
    )
}