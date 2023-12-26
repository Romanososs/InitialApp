package com.example.initialapp.android.feature.root

import android.view.MotionEvent
import androidx.annotation.FloatRange
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.sqrt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> CircularReveal(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    content: @Composable (T) -> Unit
) {
    val transition = updateTransition(targetState, "RevealAnimation")
    var offset by remember { mutableStateOf<Offset?>(null) }
    val currentlyVisible =
        remember { mutableStateListOf<T>().apply { add(transition.currentState) } }
    val contentMap = remember {
        mutableMapOf<T, @Composable () -> Unit>()
    }
    if (transition.currentState == transition.targetState) {
        // If not animating, just display the current state
        if (currentlyVisible.size != 1 || currentlyVisible[0] != transition.targetState) {
            // Remove all the intermediate items from the list once the animation is finished.
            currentlyVisible.removeAll { it != transition.targetState }
            contentMap.clear()
        }
    }
    if (!contentMap.contains(transition.targetState)) {
        // Replace target with the same key if any
        val replacementId = currentlyVisible.indexOfFirst {
            it == transition.targetState
        }
        if (replacementId == -1) {
            currentlyVisible.add(transition.targetState)
        } else {
            currentlyVisible[replacementId] = transition.targetState
        }
        contentMap.clear()
        currentlyVisible.forEachIndexed { index, stateForContent ->
            contentMap[stateForContent] = {
                val progress by transition.animateFloat(
                    transitionSpec = { animationSpec }, label = ""
                ) {
                    if (index == currentlyVisible.size - 1) {
                        if (it == stateForContent) 1f else 0f
                    } else 1f
                }
                Box(Modifier.circularReveal(progress, offset)) {
                    content(stateForContent)
                }
            }
        }
    }

    Box(modifier.pointerInteropFilter {
        offset = when (it.action) {
            MotionEvent.ACTION_DOWN -> Offset(it.x, it.y)
            else -> null
        }
        false
    }) {
        currentlyVisible.forEach() {
            key(it) {
                contentMap[it]?.invoke()
            }
        }
    }
}

fun Modifier.circularReveal(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    offset: Offset? = null
) = clip(CircularRevealShape(progress, offset))

private class CircularRevealShape(
    @FloatRange(from = 0.0, to = 1.0) private val progress: Float,
    private val offset: Offset? = null
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            addOval(
                Rect(
                    offset ?: size.center,
                    sqrt(size.height * size.height + size.width * size.width) * progress,
                )
            )
        })
    }
}