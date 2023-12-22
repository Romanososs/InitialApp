package com.example.initialapp.android.feature.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.initialapp.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(onNavigationClick: () -> Unit) {
    TopAppBar(
        title = {

        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = "Menu"
                )
            }
        })
}