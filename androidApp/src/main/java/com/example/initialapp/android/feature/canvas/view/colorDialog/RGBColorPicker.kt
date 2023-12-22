package com.example.initialapp.android.feature.canvas.view.colorDialog

import androidx.annotation.ColorInt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

@Composable
fun RGBColorPicker(
    @ColorInt color: Int,
    hexChanged: Boolean,
    onSideEffectComplete: () -> Unit,
    onValuesChange: (Int, Int, Int) -> Unit
) {
    var red by remember { mutableStateOf<Int?>(color.red) }
    var green by remember { mutableStateOf<Int?>(color.green) }
    var blue by remember { mutableStateOf<Int?>(color.blue) }
    val redInt = red ?: 0
    val greenInt = green ?: 0
    val blueInt = blue ?: 0
    LaunchedEffect(red, green, blue) {
        onValuesChange(red ?: 0, green ?: 0, blue ?: 0)
    }
    LaunchedEffect(key1 = hexChanged){
        if (hexChanged){
            red = color.red
            green = color.green
            blue = color.blue
            onSideEffectComplete()
        }
    }
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            ColorSlide(
                value = redInt.toFloat(),
                onValueChange = { red = it.toInt() },
                valueRange = 0F..255F,
                height = 20.dp,
                thumbColor = Color(redInt, 0, 0),
                trackBrush = Brush.horizontalGradient(getRedColors()),
            )
            ColorSlide(
                value = greenInt.toFloat(),
                onValueChange = { green = it.toInt() },
                valueRange = 0F..255F,
                height = 20.dp,
                thumbColor = Color(0, greenInt, 0),
                trackBrush = Brush.horizontalGradient(getGreenColors()),
            )
            ColorSlide(
                value = blueInt.toFloat(),
                onValueChange = { blue = it.toInt() },
                valueRange = 0F..255F,
                height = 20.dp,
                thumbColor = Color(0, 0, blueInt),
                trackBrush = Brush.horizontalGradient(getBlueColors()),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorTextField(
                (red ?: "").toString(),
                onValueChange = {
                    val int = it.toIntOrNull()
                    if (int == null || int in 0..255) red = int
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                title = "R",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            ColorTextField(
                (green ?: "").toString(),
                onValueChange = {
                    val int = it.toIntOrNull()
                    if (int == null || int in 0..255) green = int
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                title = "G",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            ColorTextField(
                (blue ?: "").toString(),
                onValueChange = {
                    val int = it.toIntOrNull()
                    if (int == null || int in 0..255) blue = int
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                title = "B",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

fun getRedColors(): List<Color> {
    return listOf(
        Color(0, 0, 0),
        Color(51, 0, 0),
        Color(102, 0, 0),
        Color(153, 0, 0),
        Color(204, 0, 0),
        Color(255, 0, 0),
    )
}

fun getGreenColors(): List<Color> {
    return listOf(
        Color(0, 0, 0),
        Color(0, 51, 0),
        Color(0, 102, 0),
        Color(0, 153, 0),
        Color(0, 204, 0),
        Color(0, 255, 0),
    )
}

fun getBlueColors(): List<Color> {
    return listOf(
        Color(0, 0, 0),
        Color(0, 0, 51),
        Color(0, 0, 102),
        Color(0, 0, 153),
        Color(0, 0, 204),
        Color(0, 0, 255),
    )
}
