package com.example.initialapp.android.feature.canvas.view.colorDialog

import androidx.annotation.ColorInt
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.initialapp.android.Gray100

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun ColorPicker(@ColorInt color: Int, onColorChange: (Int) -> Unit) {
    var tabState by remember { mutableIntStateOf(0) }
    var hexChanged by remember { mutableStateOf(false) } // mb use SharedFlow
    var hex by remember(color) {
        mutableStateOf(
            color.toHexString(HexFormat.UpperCase).substring(2)
        )
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = 16.dp)
        ) {
            ColorTextField(
                hex,
                onValueChange = {
                    if (it.length <= 6 && it.matches(Regex("[0-9A-Fa-f]*"))) hex = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                title = "Hex"
            ) {
                if (hex.matches(Regex("[0-9A-Fa-f]{6}"))) {
                    onColorChange(android.graphics.Color.parseColor("#$hex"))
                    hexChanged = true
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color(color))
                    .border(1.dp, Gray100)
            )
        }
        TabRow(
            selectedTabIndex = tabState,
            containerColor = MaterialTheme.colorScheme.background,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[tabState])
                        .padding(horizontal = 50.dp)
                        .height(3.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                )
            }
        ) {
            Tab(
                selected = tabState == 0,
                onClick = { tabState = 0 }) {
                Text("HSV", modifier = Modifier.padding(bottom = 4.dp))
            }
            Tab(
                selected = tabState == 1,
                onClick = { tabState = 1 }) {
                Text("RGB", modifier = Modifier.padding(bottom = 4.dp))
            }
        }
        when (tabState) {
            0 -> HSVColorPicker(color = color, hexChanged = hexChanged,
                onSideEffectComplete = { hexChanged = false },
                onValuesChange = { hue, saturation, value ->
                    val arr = floatArrayOf(hue, saturation, value)
                    onColorChange(android.graphics.Color.HSVToColor(arr))
                })

            1 -> RGBColorPicker(color = color, hexChanged = hexChanged,
                onSideEffectComplete = { hexChanged = false },
                onValuesChange = { red, green, blue ->
                    onColorChange(android.graphics.Color.argb(255, red, green, blue))
                })
        }
    }
}