package com.example.initialapp.feature.canvas.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Shape() {
    abstract val color: Color
    abstract val thickness: Float
    @Serializable
    data class Line(
        override val color: Color,
        override val thickness: Float,
        val points: List<Point>
    ) : Shape()
    @Serializable
    data class Circle(
        override val color: Color,
        override val thickness: Float,
        val fill: Boolean,
        val point: Point,
        val size: Point
    ) : Shape()
    @Serializable
    data class Square(
        override val color: Color,
        override val thickness: Float,
        val fill: Boolean,
        val point: Point,
        val size: Point
    ) : Shape()
}