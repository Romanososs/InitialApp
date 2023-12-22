package com.example.initialapp.android.feature.canvas

import androidx.compose.ui.graphics.Color as ComposeColor
import com.example.initialapp.feature.canvas.model.Color

fun Color.toColor() = ComposeColor(this.red, this.green, this.blue)