package com.example.initialapp.common.storage

import com.example.initialapp.feature.canvas.model.Color
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface SettingsDataSource {
    fun getPenToolIndex(): Int
    fun setPenToolIndex(value: Int)

    fun getShapeToolIndex(): Int
    fun setShapeToolIndex(value: Int)

    fun getColor(): String
    fun setColor(value: String)
}

class SettingsDataSourceImpl : SettingsDataSource, KoinComponent {
    companion object{
        private const val PEN_TOOL_KEY = "PEN_TOOL_KEY"
        private const val SHAPE_TOOL_KEY = "SHAPE_TOOL_KEY"
        private const val COLOR_KEY = "COLOR_KEY"
    }
    private val settings: Settings by inject()

    override fun getPenToolIndex(): Int {
        return settings.getInt(PEN_TOOL_KEY, 0)
    }

    override fun setPenToolIndex(value: Int) {
        settings[PEN_TOOL_KEY] = value
    }

    override fun getShapeToolIndex(): Int {
        return settings.getInt(SHAPE_TOOL_KEY, 0)
    }

    override fun setShapeToolIndex(value: Int) {
        settings[SHAPE_TOOL_KEY] = value
    }

    override fun getColor(): String {
        return settings.getString(COLOR_KEY, Color.Black.toHexString())
    }

    override fun setColor(value: String) {
        settings[COLOR_KEY] = value
    }

}