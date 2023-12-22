package com.example.initialapp.android.feature.canvas.view.colorDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.initialapp.android.Gray100
import com.example.initialapp.android.Gray300

@Composable
fun ColorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onDone: () -> Unit = {}
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(textAlign = TextAlign.Center),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        modifier = modifier
    ) { innerBox ->
        Row(
            modifier = Modifier
                .background(Gray100)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(title, color = Gray300)
            innerBox()
        }
    }
}