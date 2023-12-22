package com.example.initialapp.android.feature.test

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.initialapp.feature.test.TestComponent

@Composable
fun TestContent(component: TestComponent, text: String, ) {
    Text(text = text)
}