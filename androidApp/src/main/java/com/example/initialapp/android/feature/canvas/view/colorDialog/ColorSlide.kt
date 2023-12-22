package com.example.initialapp.android.feature.canvas.view.colorDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.initialapp.android.Gray100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSlide(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    height: Dp = 30.dp,
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    trackBrush: Brush = SolidColor(MaterialTheme.colorScheme.surface),
    trackHorizontalPadding: Dp = 0.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    CompositionLocalProvider(
        LocalMinimumInteractiveComponentEnforcement provides false,
    ) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth(),
            thumb = {
                Spacer(
                    modifier = Modifier
                        .size(height)
                        .indication(
                            interactionSource = interactionSource,
                            indication = rememberRipple(
                                bounded = false,
                                radius = 0.dp
                            )
                        )
                        .hoverable(interactionSource = interactionSource)
                        .shadow(5.dp, RoundedCornerShape(100), clip = false)
                        .background(thumbColor, RoundedCornerShape(100))
                        .border(2.dp, Gray100, RoundedCornerShape(100))
                )
            },
            track = {
                Spacer(
                    modifier = Modifier
                        .padding(horizontal = trackHorizontalPadding)
                        .height(height)
                        .fillMaxWidth()
                        .background(
                            brush = trackBrush,
                            shape = RoundedCornerShape(40)
                        )
                )
            }
        )
    }
}