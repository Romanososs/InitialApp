package com.example.initialapp.feature.canvas.model

import kotlinx.serialization.Serializable

@Serializable
data class Point(val x: Float, val y: Float) {
    operator fun plus(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    operator fun minus(other: Point): Point = Point(this.x - other.x, this.y - other.y)
}
