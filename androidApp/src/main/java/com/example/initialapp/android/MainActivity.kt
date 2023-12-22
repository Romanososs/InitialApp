package com.example.initialapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.initialapp.android.feature.root.RootContent
import com.example.initialapp.feature.root.RootComponentImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RootComponentImpl(defaultComponentContext())
        setContent {
            Theme {
                RootContent(root)
            }
        }
    }
}

