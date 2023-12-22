package com.example.initialapp.feature.canvas.state

import kotlinx.serialization.Serializable

sealed interface Tool

@Serializable
sealed class PenTool : Tool {
    abstract val thickness: Float
    abstract val minThickness: Float
    abstract val maxThickness: Float
    @Serializable
    data class Pencil(
        override val thickness: Float,
        override val minThickness: Float,
        override val maxThickness: Float
    ) : PenTool()
    @Serializable
    data class Eraser(
        override val thickness: Float,
        override val minThickness: Float,
        override val maxThickness: Float
    ) : PenTool()
}

@Serializable
sealed class ShapeTool : Tool {
    @Serializable
    data object Line : ShapeTool()
    @Serializable
    sealed class Fillable : ShapeTool() {
        abstract val fill: Boolean
        @Serializable
        data class Circle(override val fill: Boolean) : Fillable()
        @Serializable
        data class Square(override val fill: Boolean) : Fillable()
    }
}