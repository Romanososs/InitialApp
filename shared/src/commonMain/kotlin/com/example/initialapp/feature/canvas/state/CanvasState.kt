package com.example.initialapp.feature.canvas.state

import com.example.initialapp.feature.canvas.model.CanvasInfo
import com.example.initialapp.feature.canvas.model.Color
import com.example.initialapp.feature.canvas.model.Shape

data class CanvasState(
    val penTools: List<PenTool> = getDefaultPenTools(),
    val shapeTools: List<ShapeTool> = getDefaultShapeTools(),

    val currentPenTool: PenTool = penTools.first(),
    val currentShapeTool: ShapeTool = shapeTools.first(),
    val currentColor: Color = Color.Black,

    val saveResult: Result? = null,

    val shapes: List<Shape> = listOf()
) {
    fun copy(info: CanvasInfo) = this.copy(
        penTools = info.penTools,
        shapeTools = info.shapeTools,
        currentPenTool = info.currentPenTool,
        currentShapeTool = info.currentShapeTool,
        currentColor = info.currentColor,
        shapes = info.shapes
    )

    fun toCanvasInfo() = CanvasInfo(
        penTools = penTools,
        shapeTools = shapeTools,
        currentPenTool = currentPenTool,
        currentShapeTool = currentShapeTool,
        currentColor = currentColor,
        shapes = shapes
    )
}

fun getDefaultPenTools() = listOf(
    PenTool.Pencil(20f, 1f, 50f),
    PenTool.Eraser(50f, 1f, 100f)
)

fun getDefaultShapeTools() = listOf(
    ShapeTool.Line,
    ShapeTool.Fillable.Circle(false),
    ShapeTool.Fillable.Square(false)
)


