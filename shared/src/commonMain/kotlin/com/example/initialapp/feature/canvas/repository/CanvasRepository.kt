package com.example.initialapp.feature.canvas.repository

import com.example.initialapp.common.storage.DBDataSource
import com.example.initialapp.common.storage.SettingsDataSource
import com.example.initialapp.feature.canvas.model.CanvasInfo
import com.example.initialapp.feature.canvas.model.toColor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface CanvasRepository {
    fun getCanvasInfo(): CanvasInfo
    fun setCanvasInfo(value: CanvasInfo)
}

class CanvasRepositoryImpl: CanvasRepository, KoinComponent {
    private val db: DBDataSource by inject()
    private val settings: SettingsDataSource by inject()

    override fun getCanvasInfo(): CanvasInfo {
        val penTools = db.getPenTools()
        val shapeTools = db.getShapeTools()
        return CanvasInfo(
            penTools = penTools,
            shapeTools = shapeTools,
            shapes = db.getImage(),
            //can be made with id instead of index
            currentPenTool = penTools[settings.getPenToolIndex()],
            currentShapeTool = shapeTools[settings.getShapeToolIndex()],
            currentColor = settings.getColor().toColor()
        )
    }

    override fun setCanvasInfo(value: CanvasInfo) {
        db.setPenTools(value.penTools)
        db.setShapeTools(value.shapeTools)
        db.setImage(value.shapes)
        settings.setPenToolIndex(value.penTools.indexOf(value.currentPenTool))
        settings.setShapeToolIndex(value.shapeTools.indexOf(value.currentShapeTool))
        settings.setColor(value.currentColor.toHexString())
    }

}