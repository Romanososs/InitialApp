package com.example.initialapp.android.feature.canvas.view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDialog(text: String, onConfirmClick: () -> Unit, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(5.dp)
            )
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Divider()
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentEnforcement provides false,
            ) {
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    TextButton(
                        onClick = onDismissRequest,
                        shape = RoundedCornerShape(0.dp, 0.dp, 5.dp, 0.dp),
                        modifier = Modifier.weight(1F)
                    ) {
                        Text("CANCEL")
                    }
                    Spacer(
                        modifier = Modifier
                            .background(DividerDefaults.color)
                            .width(1.dp)
                            .fillMaxHeight()
                    )

                    TextButton(
                        onClick = onConfirmClick,
                        shape = RoundedCornerShape(0.dp, 0.dp, 5.dp, 0.dp),
                        modifier = Modifier.weight(1F)
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ConfirmDialogPreview() {
    ConfirmDialog("Are you sure yo want to create new sketch?\nThe current one will be lost", {}) {}
}