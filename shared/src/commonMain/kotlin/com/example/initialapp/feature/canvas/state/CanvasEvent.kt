package com.example.initialapp.feature.canvas.state

import com.example.initialapp.feature.canvas.model.Color
import com.example.initialapp.feature.canvas.model.Point

sealed class CanvasEvent(){
    data object CreateNew: CanvasEvent()
    data class SaveCurrent(val image: ByteArray): CanvasEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as SaveCurrent

            if (!image.contentEquals(other.image)) return false

            return true
        }

        override fun hashCode(): Int {
            return image.contentHashCode()
        }
    }

    data object Undo: CanvasEvent()
    data object Redo: CanvasEvent()
    data class SetPenTool(val tool: PenTool): CanvasEvent()
    data class SetShapeTool(val tool: ShapeTool): CanvasEvent()
    data class Draw(val points: List<Point>): CanvasEvent()
    data class SetColor(val color: Color): CanvasEvent()

    data class SaveImageResult(val result: Result): CanvasEvent()
    data object DismissDialog: CanvasEvent()
}
