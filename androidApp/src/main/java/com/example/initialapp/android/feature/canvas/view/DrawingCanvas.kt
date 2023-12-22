package com.example.initialapp.android.feature.canvas.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.initialapp.android.feature.canvas.toColor
import com.example.initialapp.feature.canvas.model.Point
import com.example.initialapp.feature.canvas.model.Shape
import com.example.initialapp.feature.canvas.state.CanvasEvent
import com.example.initialapp.feature.canvas.state.CanvasState
import com.example.initialapp.feature.canvas.state.PenTool
import com.example.initialapp.feature.canvas.state.ShapeTool

@Composable
fun DrawingCanvas(
    state: CanvasState,
    modifier: Modifier = Modifier,
    onDraw: (CanvasEvent.Draw) -> Unit
) {
    val inputPoints = remember { mutableStateListOf<Offset>() }

    Canvas(modifier = modifier
        .fillMaxSize()
        .clipToBounds()
        .background(Color.White)
        .pointerInput(Unit) {
            detectTapGestures {
                onDraw(CanvasEvent.Draw(listOf(Point(it.x, it.y), Point(it.x, it.y))))
            }
        }
        .pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                inputPoints.add(it)
            }, onDragEnd = {
                onDraw(CanvasEvent.Draw(inputPoints.map { Point(it.x, it.y) }))
                inputPoints.clear()
            }) { change, _ ->
                inputPoints.add(change.position)
            }
        }
    ) {
        state.shapes.forEach { shape ->
            val stroke = Stroke(
                width = shape.thickness,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
            when (shape) {
                is Shape.Line -> drawPath(
                    getPath(shape.points.map { Offset(it.x, it.y) }),
                    color = shape.color.toColor(),
                    style = stroke
                )

                is Shape.Circle -> drawOval(
                    color = shape.color.toColor(),
                    topLeft = Offset(shape.point.x, shape.point.y),
                    size = Size(shape.size.x, shape.size.y),
                    style = if (shape.fill) Fill else stroke
                )

                is Shape.Square -> drawRect(
                    color = shape.color.toColor(),
                    topLeft = Offset(shape.point.x, shape.point.y),
                    size = Size(shape.size.x, shape.size.y),
                    style = if (shape.fill) Fill else stroke
                )
            }
        }
        val stroke = Stroke(
            width = state.currentPenTool.thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        if (inputPoints.size > 1) {
            when (state.currentShapeTool) {
                is ShapeTool.Line -> drawPath(
                    getPath(inputPoints),
                    color = if (state.currentPenTool is PenTool.Eraser) Color.White else state.currentColor.toColor(),
                    style = stroke
                )

                is ShapeTool.Fillable.Circle -> drawOval(
                    color = if (state.currentPenTool is PenTool.Eraser) Color.White else state.currentColor.toColor(),
                    topLeft = inputPoints.first(),
                    size = Size(
                        inputPoints.last().x - inputPoints.first().x,
                        inputPoints.last().y - inputPoints.first().y
                    ),
                    style = if ((state.currentShapeTool as ShapeTool.Fillable.Circle).fill) Fill else stroke
                )

                is ShapeTool.Fillable.Square -> drawRect(
                    color = if (state.currentPenTool is PenTool.Eraser) Color.White else state.currentColor.toColor(),
                    topLeft = inputPoints.first(),
                    size = Size(
                        inputPoints.last().x - inputPoints.first().x,
                        inputPoints.last().y - inputPoints.first().y
                    ),
                    style = if ((state.currentShapeTool as ShapeTool.Fillable.Square).fill) Fill else stroke
                )
            }
        }
    }
}


fun getPath(points: List<Offset>): Path {
    return Path().apply {
        if (points.size > 1) {
            var oldPoint: Offset? = null
            this.moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                val point: Offset = points[i]
                oldPoint?.let { old ->
                    val midPoint = Offset((old.x + point.x) / 2, (old.y + point.y) / 2)
                    this.quadraticBezierTo(old.x, old.y, midPoint.x, midPoint.y)
                }
                oldPoint = point
            }
            oldPoint?.let { this.lineTo(it.x, oldPoint.y) }
        }
    }
}