package com.example.initialapp.feature.canvas.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnPause
import com.example.initialapp.feature.canvas.model.Color
import com.example.initialapp.feature.canvas.model.Point
import com.example.initialapp.feature.canvas.model.Shape
import com.example.initialapp.feature.canvas.repository.CanvasRepository
import com.example.initialapp.feature.canvas.state.CanvasEvent
import com.example.initialapp.feature.canvas.state.CanvasState
import com.example.initialapp.feature.canvas.state.PenTool
import com.example.initialapp.feature.canvas.state.Result
import com.example.initialapp.feature.canvas.state.ShapeTool
import com.example.initialapp.feature.canvas.useCase.MediaUseCase
import com.example.initialapp.navigation.componentCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CanvasComponent {
    val state: StateFlow<CanvasState>
    fun handleEvent(event: CanvasEvent)
}

class CanvasComponentImpl(
    context: ComponentContext
) : CanvasComponent, ComponentContext by context, KoinComponent {

    private val mediaUC: MediaUseCase by inject()
    private val canvasRepo: CanvasRepository by inject()

    private val scope = componentCoroutineScope()
    private val undoList: MutableList<Shape> = mutableListOf()

    private val _state = MutableStateFlow(CanvasState())
    override val state: StateFlow<CanvasState>
        get() = _state

    init {
        lifecycle.doOnCreate {
            scope.launch {
                val info = canvasRepo.getCanvasInfo()
                _state.update { it.copy(info) }
            }
        }
        lifecycle.doOnPause {
            scope.launch {
                canvasRepo.setCanvasInfo(_state.value.toCanvasInfo())
            }
        }
    }

    override fun handleEvent(event: CanvasEvent) {
        when (event) {
            CanvasEvent.CreateNew -> createNew()
            is CanvasEvent.SaveCurrent -> saveCurrent(event.image)
            CanvasEvent.Undo -> undo()
            CanvasEvent.Redo -> redo()
            is CanvasEvent.Draw -> draw(event.points)
            is CanvasEvent.SetColor -> setColor(event.color)
            is CanvasEvent.SetPenTool -> setPenTool(event.tool)
            is CanvasEvent.SetShapeTool -> setShapeTool(event.tool)
            is CanvasEvent.SaveImageResult -> setImageResult(event.result)
            CanvasEvent.DismissDialog -> dismissDialog()
        }
    }

    private fun createNew() {
        _state.update { it.copy(shapes = listOf()) }
    }

    private fun saveCurrent(byteArray: ByteArray) {
        setImageResult(if (mediaUC.saveImage(byteArray)) Result.Success else Result.Fail)
    }

    private fun undo() {
        val shapes = _state.value.shapes
        if (shapes.isNotEmpty()) {
            undoList.add(shapes.last())
            _state.update { it.copy(shapes = it.shapes.dropLast(1)) }
        }
    }

    private fun redo() {
        if (undoList.isNotEmpty()) {
            _state.update { it.copy(shapes = it.shapes + undoList.last()) }
            undoList.removeLast()
        }
    }

    /**
     * points should have minimum 2 elements
     */
    private fun draw(points: List<Point>) {
        undoList.clear()
        when (val tool = _state.value.currentShapeTool) {
            is ShapeTool.Line -> _state.addShape(
                Shape.Line(
                    if (_state.value.currentPenTool is PenTool.Eraser) Color.White else _state.value.currentColor,
                    _state.value.currentPenTool.thickness,
                    points
                )
            )

            is ShapeTool.Fillable.Circle -> _state.addShape(
                Shape.Circle(
                    if (_state.value.currentPenTool is PenTool.Eraser) Color.White else _state.value.currentColor,
                    _state.value.currentPenTool.thickness,
                    tool.fill,
                    points.first(),
                    points.last() - points.first()
                )
            )

            is ShapeTool.Fillable.Square -> _state.addShape(
                Shape.Square(
                    if (_state.value.currentPenTool is PenTool.Eraser) Color.White else _state.value.currentColor,
                    _state.value.currentPenTool.thickness,
                    tool.fill,
                    points.first(),
                    points.last() - points.first()
                )
            )
        }
    }

    private fun setColor(color: Color) {
        _state.update { it.copy(currentColor = color) }
    }

    private fun setPenTool(tool: PenTool) {
        _state.update { it.copy(currentPenTool = tool) }
        when (tool) {
            is PenTool.Eraser -> _state.updatePenTools<PenTool.Eraser>(tool)
            is PenTool.Pencil -> _state.updatePenTools<PenTool.Pencil>(tool)
        }
    }

    private fun setShapeTool(tool: ShapeTool) {
        _state.update { it.copy(currentShapeTool = tool) }
        when (tool) {
            is ShapeTool.Line -> _state.updateShapeTools<ShapeTool.Line>(tool)
            is ShapeTool.Fillable.Circle -> _state.updateShapeTools<ShapeTool.Fillable.Circle>(tool)
            is ShapeTool.Fillable.Square -> _state.updateShapeTools<ShapeTool.Fillable.Square>(tool)
        }
    }

    private fun setImageResult(result: Result) {
        _state.update { it.copy(saveResult = result) }
    }

    private fun dismissDialog() {
        _state.update { it.copy(saveResult = null) }
    }

    private inline fun <reified T : PenTool> MutableStateFlow<CanvasState>.updatePenTools(tool: PenTool) {
        this.update { lState ->
            lState.copy(penTools = lState.penTools.toMutableList().also { l ->
                l[lState.penTools.indexOfFirst { t -> t is T }] = tool
            })
        }
    }

    private inline fun <reified T : ShapeTool> MutableStateFlow<CanvasState>.updateShapeTools(tool: ShapeTool) {
        this.update { lState ->
            lState.copy(shapeTools = lState.shapeTools.toMutableList().also { l ->
                l[lState.shapeTools.indexOfFirst { t -> t is T }] = tool
            })
        }
    }

    private fun MutableStateFlow<CanvasState>.addShape(shape: Shape) {
        this.update { lState ->
            lState.copy(shapes = lState.shapes + shape)
        }
    }

}


