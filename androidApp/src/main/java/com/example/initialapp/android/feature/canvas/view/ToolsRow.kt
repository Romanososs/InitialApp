package com.example.initialapp.android.feature.canvas.view

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.initialapp.android.R
import com.example.initialapp.android.feature.canvas.view.colorDialog.ColorDialog
import com.example.initialapp.android.feature.canvas.view.dialog.ConfirmDialog
import com.example.initialapp.android.feature.canvas.view.drawingMenu.DrawingItemData
import com.example.initialapp.android.feature.canvas.view.drawingMenu.DrawingMenu
import com.example.initialapp.android.feature.canvas.view.drawingMenu.PenDialog
import com.example.initialapp.android.feature.canvas.view.drawingMenu.ShapeDialog
import com.example.initialapp.feature.canvas.state.CanvasEvent
import com.example.initialapp.feature.canvas.state.CanvasState
import com.example.initialapp.feature.canvas.state.PenTool
import com.example.initialapp.feature.canvas.state.ShapeTool
import com.example.initialapp.feature.canvas.state.Tool

@Composable
fun ToolsRow(
    state: CanvasState,
    onNavigationClick: () -> Unit,
    onEvent: (CanvasEvent) -> Unit,
) {
    var isMenuExpended by remember { mutableStateOf(false) }
    var isConfirmShown by remember { mutableStateOf(false) }
    var isPencilExpended by remember { mutableStateOf(false) }
    var isShapesExpended by remember { mutableStateOf(false) }
    var isColorsExpended by remember { mutableStateOf(false) }
    var popupState by remember { mutableStateOf<Tool?>(null) }

    val penMenu = listOf(
        DrawingItemData(iconId = R.drawable.ic_pencil, text = "Pencil") {
            popupState = state.penTools[0]
        },
        DrawingItemData(iconId = R.drawable.ic_eraser, text = "Eraser") {
            popupState = state.penTools[1]
        }
    )
    val shapeMenu = listOf(
        DrawingItemData(iconId = R.drawable.ic_line, text = "Line") {
            onEvent(CanvasEvent.SetShapeTool(ShapeTool.Line))
            isShapesExpended = false
        },
        DrawingItemData(iconId = R.drawable.ic_circle, text = "Circle") {
            popupState = state.shapeTools[1]
        },
        DrawingItemData(iconId = R.drawable.ic_square, text = "Square") {
            popupState = state.shapeTools[2]
        }
    )

    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)

        ) {
            IconButton(
                onClick = onNavigationClick,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Open navigation menu"
                )
            }
            Spacer(
                modifier = Modifier
                    .background(DividerDefaults.color)
                    .width(1.dp)
                    .fillMaxHeight()
            )
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Box {
                    IconButton(onClick = { isMenuExpended = !isMenuExpended }) {
                        Icon(
                            painterResource(id = R.drawable.ic_list),
                            contentDescription = "Open menu",
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuExpended,
                        onDismissRequest = { isMenuExpended = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_add),
                                    contentDescription = "Create new"
                                )
                            },
                            text = { Text("Create new") },
                            onClick = {
                                isConfirmShown = true
                                isMenuExpended = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_save),
                                    contentDescription = "Save"
                                )
                            },
                            text = { Text("Save") },
                            onClick = {
                                onEvent(CanvasEvent.SaveCurrent(ByteArray(0)))
                                isMenuExpended = false
                            }
                        )
                    }
                }


                IconButton(onClick = { onEvent(CanvasEvent.Undo) }) {
                    Icon(
                        painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Undo",
                    )
                }
                IconButton(onClick = { onEvent(CanvasEvent.Redo) }) {
                    Icon(
                        painterResource(id = R.drawable.ic_arrow_forward),
                        contentDescription = "Redo",
                    )
                }
                Box {
                    IconButton(onClick = { isPencilExpended = !isPencilExpended }) {
                        Icon(
                            painterResource(id = R.drawable.ic_draw_pencil),
                            contentDescription = "Choose pencil",
                        )
                    }
                    DrawingMenu(
                        buttons = penMenu,
                        visible = isPencilExpended,
                        onDismissRequest = { isPencilExpended = false }
                    )
                }
                Box {
                    IconButton(onClick = { isShapesExpended = !isShapesExpended }) {
                        Icon(
                            painterResource(id = R.drawable.ic_shapes),
                            contentDescription = "Choose shape",
                        )
                    }
                    DrawingMenu(
                        buttons = shapeMenu,
                        visible = isShapesExpended,
                        onDismissRequest = { isShapesExpended = false }
                    )
                }
                IconButton(onClick = { isColorsExpended = !isColorsExpended }) {
                    Icon(
                        painterResource(id = R.drawable.ic_color_wheel),
                        contentDescription = "Choose color",
                        tint = Color.Unspecified
                    )
                }
            }
        }

        popupState?.let { tool ->
            when (tool) {
                is PenTool -> PenDialog(
                    value = tool,
                    onDismissRequest = {
                        popupState = null
                        isPencilExpended = false
                    },
                    onConfirmClick = {
                        onEvent(CanvasEvent.SetPenTool(it))
                        isPencilExpended = false
                    }
                )

                is ShapeTool.Fillable -> ShapeDialog(
                    value = tool,
                    onDismissRequest = {
                        popupState = null
                        isShapesExpended = false
                    },
                    onConfirmClick = {
                        onEvent(CanvasEvent.SetShapeTool(it))
                        isShapesExpended = false
                    }
                )

                else -> {}
            }
        }
        if (isColorsExpended) {
            ColorDialog(
                color = state.currentColor,
                onDismissRequest = { isColorsExpended = false }) {
                onEvent(CanvasEvent.SetColor(it))
            }
        }
        if (isConfirmShown) {
            ConfirmDialog(
                text = "Are you sure yo want to create new sketch?\nThe current one will be lost",
                onConfirmClick = {
                    onEvent(CanvasEvent.CreateNew)
                    isConfirmShown = false
                }
            ) {
                isConfirmShown = false
            }
        }
    }
}

@Preview
@Composable
fun ToolsRowPreview() {
    val state by remember { mutableStateOf(CanvasState()) }
    ToolsRow(state = state, onNavigationClick = {}) {

    }
}