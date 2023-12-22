package com.example.initialapp.android.feature.canvas.view.colorDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.initialapp.android.feature.canvas.view.dialog.DialogButton
import com.example.initialapp.feature.canvas.model.Color

@Composable
fun ColorDialog(
    color: Color,
    onDismissRequest: () -> Unit,
    onConfirmClick: (Color) -> Unit
) {
    var colorState by remember {
        mutableIntStateOf(
            android.graphics.Color.valueOf(
                color.red,
                color.green,
                color.blue
            ).toArgb()
        )
    }
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(5.dp))
        ) {
            ColorPicker(colorState) {
                colorState = it
            }
            DialogButton {
                onConfirmClick(
                    Color(colorState.red / 255F, colorState.green / 255F, colorState.blue / 255F)
                )
                onDismissRequest()
            }
        }
    }
}

@Preview
@Composable
fun ColorPickerPreview() {
    ColorDialog(Color.Black, {}) {}
}