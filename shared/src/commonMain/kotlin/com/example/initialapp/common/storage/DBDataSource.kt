package com.example.initialapp.common.storage

import com.example.initialapp.Database
import com.example.initialapp.ImageQueries
import com.example.initialapp.PenToolsQueries
import com.example.initialapp.ShapeToolsQueries
import com.example.initialapp.feature.canvas.model.Shape
import com.example.initialapp.feature.canvas.state.PenTool
import com.example.initialapp.feature.canvas.state.ShapeTool
import com.example.initialapp.feature.canvas.state.getDefaultPenTools
import com.example.initialapp.feature.canvas.state.getDefaultShapeTools
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface DBDataSource {
    fun getPenTools(): List<PenTool>
    fun setPenTools(value: List<PenTool>)

    fun getShapeTools(): List<ShapeTool>
    fun setShapeTools(value: List<ShapeTool>)

    fun getImage(): List<Shape>
    fun setImage(value: List<Shape>)
}

class DBDataSourceImpl : DBDataSource, KoinComponent {
    private val json: Json by inject()
    private val database: Database by inject()

    private val penToolsQueries: PenToolsQueries = database.penToolsQueries
    private val shapeToolsQueries: ShapeToolsQueries = database.shapeToolsQueries
    private val imageQueries: ImageQueries = database.imageQueries

    override fun getPenTools(): List<PenTool> {
        return penToolsQueries.selectAll { _, tool ->
            json.decodeFromString(PenTool.serializer(), tool)
        }.executeAsList().ifEmpty { getDefaultPenTools() }
    }

    override fun setPenTools(value: List<PenTool>) {
        penToolsQueries.transaction {
            penToolsQueries.deleteAll()
            value.forEach {
                penToolsQueries.insert(json.encodeToString(PenTool.serializer(), it))
            }
        }
    }

    override fun getShapeTools(): List<ShapeTool> {
        return shapeToolsQueries.selectAll { _, tool ->
            json.decodeFromString(ShapeTool.serializer(), tool)
        }.executeAsList().ifEmpty { getDefaultShapeTools() }
    }

    override fun setShapeTools(value: List<ShapeTool>) {
        shapeToolsQueries.transaction {
            shapeToolsQueries.deleteAll()
            value.forEach {
                shapeToolsQueries.insert(json.encodeToString(ShapeTool.serializer(), it))
            }
        }
    }

    override fun getImage(): List<Shape> {
        return imageQueries.selectAll { _, shapes ->
            json.decodeFromString(Shape.serializer(), shapes)
        }.executeAsList()
    }

    override fun setImage(value: List<Shape>) {
        imageQueries.transaction {
            imageQueries.deleteAll()
            value.forEach {
                imageQueries.insert(json.encodeToString(Shape.serializer(), it))
            }
        }
    }

}