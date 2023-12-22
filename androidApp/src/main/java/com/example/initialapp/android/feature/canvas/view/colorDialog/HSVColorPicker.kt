package com.example.initialapp.android.feature.canvas.view.colorDialog

import androidx.annotation.ColorInt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HSVColorPicker(
    @ColorInt color: Int,
    hexChanged: Boolean,
    onSideEffectComplete: () -> Unit,
    onValuesChange: (Float, Float, Float) -> Unit
) {
    var hue by remember {
        val arr = FloatArray(3)
        android.graphics.Color.colorToHSV(color, arr)
        mutableFloatStateOf(arr[0])
    }
    var saturation by remember {
        val arr = FloatArray(3)
        android.graphics.Color.colorToHSV(color, arr)
        mutableFloatStateOf(arr[1])
    }
    var value by remember {
        val arr = FloatArray(3)
        android.graphics.Color.colorToHSV(color, arr)
        mutableFloatStateOf(arr[2])
    }
    LaunchedEffect(hue, saturation, value) {
        onValuesChange(hue, saturation, value)
    }
    LaunchedEffect(key1 = hexChanged){
        if (hexChanged){
            val arr = FloatArray(3)
            android.graphics.Color.colorToHSV(color, arr)
            hue = arr[0]
            saturation = arr[1]
            value = arr[2]
            onSideEffectComplete()
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        ColorSlide(
            value = hue,
            onValueChange = { hue = it },
            valueRange = 0F..360F,
            height = 30.dp,
            thumbColor = Color.hsv(hue, 1F, 1F),
            trackBrush = Brush.horizontalGradient(getHueColors())
        )
        ColorSlide(
            value = saturation,
            onValueChange = { saturation = it },
            valueRange = 0F..1F,
            height = 20.dp,
            thumbColor = Color.hsv(hue, saturation, value),
            trackBrush = Brush.horizontalGradient(getSaturationColors(hue, value)),
            trackHorizontalPadding = 5.dp // hueSlider.height / 2 - saturationSlider.height / 2
        )
        ColorSlide(
            value = value,
            onValueChange = { value = it },
            valueRange = 0F..1F,
            height = 20.dp,
            thumbColor = Color.hsv(hue, saturation, value),
            trackBrush = Brush.horizontalGradient(getValueColors(hue, saturation)),
            trackHorizontalPadding = 5.dp // hueSlider.height / 2 - valueSlider.height / 2
        )
    }
}

fun getHueColors(): List<Color> {
    return listOf(
        Color.hsv(0F, 1F, 1F),
        Color.hsv(60F, 1F, 1F),
        Color.hsv(120F, 1F, 1F),
        Color.hsv(180F, 1F, 1F),
        Color.hsv(240F, 1F, 1F),
        Color.hsv(300F, 1F, 1F),
        Color.hsv(360F, 1F, 1F),
    )
}

fun getSaturationColors(hue: Float, value: Float): List<Color> {
    return listOf(
        Color.hsv(hue, 0f, value),
        Color.hsv(hue, 0.2f, value),
        Color.hsv(hue, 0.4f, value),
        Color.hsv(hue, 0.6f, value),
        Color.hsv(hue, 0.8f, value),
        Color.hsv(hue, 1f, value),
    )
}

fun getValueColors(hue: Float, saturation: Float): List<Color> {
    return listOf(
        Color.hsv(hue, saturation, 0f),
        Color.hsv(hue, saturation, 0.2f),
        Color.hsv(hue, saturation, 0.4f),
        Color.hsv(hue, saturation, 0.6f),
        Color.hsv(hue, saturation, 0.8f),
        Color.hsv(hue, saturation, 1f),
    )
}