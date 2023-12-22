@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.initialapp.android.feature.canvas.view.drawingMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.initialapp.android.feature.canvas.view.dialog.DialogButton
import com.example.initialapp.feature.canvas.state.PenTool
import com.example.initialapp.feature.canvas.state.ShapeTool

@Composable
fun PenDialog(
    value: PenTool,
    onDismissRequest: () -> Unit,
    onConfirmClick: (PenTool) -> Unit
) {
    var sliderState by remember { mutableFloatStateOf(value.thickness) }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(5.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Thickness",
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(
                        text = String.format("%.1f", sliderState),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                Slider(
                    value = sliderState,
                    onValueChange = { sliderState = it },
                    valueRange = value.minThickness..value.maxThickness,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                )
            }
            DialogButton{
                onConfirmClick(
                    when (value) {
                        is PenTool.Eraser -> value.copy(thickness = sliderState)
                        is PenTool.Pencil -> value.copy(thickness = sliderState)
                    }
                )
                onDismissRequest()
            }
        }
    }
}

@Composable
fun ShapeDialog(
    value: ShapeTool.Fillable,
    onDismissRequest: () -> Unit,
    onConfirmClick: (ShapeTool.Fillable) -> Unit
) {
    var checkBoxState by remember { mutableStateOf(value.fill) }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(5.dp))
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Checkbox(checked = checkBoxState, onCheckedChange = { checkBoxState = it })
                Text("Fill")
            }
            DialogButton {
                onConfirmClick(
                    when (value) {
                        is ShapeTool.Fillable.Circle -> value.copy(fill = checkBoxState)
                        is ShapeTool.Fillable.Square -> value.copy(fill = checkBoxState)

                    }
                )
                onDismissRequest()
            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    PenDialog(
        value = PenTool.Pencil(2f, 0.1f, 10f),
        onDismissRequest = {},
        onConfirmClick = {})

}

@Preview
@Composable
fun Dialog2Preview() {
    ShapeDialog(value = ShapeTool.Fillable.Square(false), onDismissRequest = {}, onConfirmClick = {})
}