package com.example.initialapp.feature.canvas.model

import com.example.initialapp.feature.canvas.state.PenTool
import com.example.initialapp.feature.canvas.state.ShapeTool

data class CanvasInfo(
    val penTools: List<PenTool>,
    val shapeTools: List<ShapeTool>,

    val currentPenTool: PenTool,
    val currentShapeTool: ShapeTool,
    val currentColor: Color,
    val shapes: List<Shape>
)