package com.example.initialapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.defaultComponentContext
import com.example.initialapp.android.feature.root.RootContent
import com.example.initialapp.feature.root.component.RootComponentImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RootComponentImpl(defaultComponentContext())
        setContent {
            RootContent(root)
        }
    }
}

