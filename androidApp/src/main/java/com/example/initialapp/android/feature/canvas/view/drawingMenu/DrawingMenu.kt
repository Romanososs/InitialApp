package com.example.initialapp.android.feature.canvas.view.drawingMenu

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

data class DrawingItemData(
    val text: String,
    val iconId: Int,
    val onClick: () -> Unit
)

@Composable
fun DrawingMenu(
    buttons: List<DrawingItemData>,
    visible: Boolean = false,
    onDismissRequest: () -> Unit = {},
    rowCount: Int = 3
) {
    val density = LocalDensity.current
    val expandedState = remember { MutableTransitionState(visible) }
    LaunchedEffect(key1 = visible) {
        expandedState.targetState = visible
    }
    val transition = updateTransition(expandedState, "Popup")
    val scale by transition.animateFloat(label = "Popup Scale") {
        if (it) 1f else 0f
    }
    val alpha by transition.animateFloat(label = "Popup Alpha") {
        if (it) 1f else 0f
    }
    var menuOffset by remember { mutableStateOf(IntOffset(0, 0)) }

    if (expandedState.currentState || expandedState.targetState) {
        Popup(
            offset = menuOffset,
            alignment = Alignment.BottomCenter,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                        transformOrigin = TransformOrigin(0.5f, 0f)
                    }
                    .onGloballyPositioned {
                        menuOffset =
                            with(density) { IntOffset(0, it.size.height + 4.dp.roundToPx()) }
                    }
            ) {
                for (row in 0..(buttons.size.toDouble() / rowCount).toInt()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        val lastOffsetIndex = row * rowCount + rowCount
                        buttons.subList(
                            row * rowCount,
                            if (lastOffsetIndex > buttons.size) buttons.size else lastOffsetIndex
                        ).forEach {
                            DrawingItem(
                                icon = painterResource(id = it.iconId),
                                text = it.text,
                                onClick = it.onClick
                            )
                        }
                    }
                }
            }
        }
    }
}