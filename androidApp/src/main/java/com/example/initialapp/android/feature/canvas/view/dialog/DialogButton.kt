package com.example.initialapp.android.feature.canvas.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogButton(
    shape: Shape = RoundedCornerShape(0.dp, 0.dp, 5.dp, 5.dp),
    modifier: Modifier = Modifier,
    onClick: ()-> Unit
) {
    Column {
        Divider()
        CompositionLocalProvider(
            LocalMinimumInteractiveComponentEnforcement provides false,
        ) {
            TextButton(
                onClick = onClick,
                shape = shape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("OK")
            }
        }
    }
}