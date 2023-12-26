package com.example.initialapp.android.feature.root

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.example.initialapp.android.R
import com.example.initialapp.android.Theme
import com.example.initialapp.android.feature.canvas.CanvasContent
import com.example.initialapp.android.feature.test.TestContent
import com.example.initialapp.feature.root.component.RootComponent
import com.example.initialapp.feature.root.component.RootComponent.Child
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RootContent(
    component: RootComponent
) {
    val childStack by component.stack.subscribeAsState()
    val state by component.state.collectAsState()
    val activeComponent = childStack.active.instance

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isSystemDark = isSystemInDarkTheme()
    var darkTheme: Boolean by remember(state.isDarkMode) {
        mutableStateOf(state.isDarkMode ?: isSystemDark)
    }

    CircularReveal(darkTheme) { theme ->
        Theme(theme) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = activeComponent !is Child.CanvasChild || drawerState.isOpen,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier.width(300.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                IconButton(
                                    onClick = { component.onDarkModeToggle(!darkTheme) },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_dark_mode),
                                        contentDescription = "Dark Mode"
                                    )
                                }
                            }
                            Divider()
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                NavigationDrawerItem(
                                    label = { Text(text = "Canvas") },
                                    selected = activeComponent is Child.CanvasChild,
                                    onClick = {
                                        component.onCanvasButtonClick()
                                        drawerState.closeIn(scope)
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text(text = "Test1") },
                                    selected = activeComponent is Child.Test1Child,
                                    onClick = {
                                        component.onTest1ButtonClick()
                                        drawerState.closeIn(scope)
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text(text = "Test2") },
                                    selected = activeComponent is Child.Test2Child,
                                    onClick = {
                                        component.onTest2ButtonClick()
                                        drawerState.closeIn(scope)
                                    }
                                )
                            }
                        }
                    }
                ) {
                    Children(
                        stack = component.stack,
                    ) {
                        when (val child = it.instance) {
                            is Child.CanvasChild -> CanvasContent(child.component,
                                onNavigationClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                })

                            is Child.Test1Child -> TestContent(
                                child.component,
                                text = "TestContent1"
                            )

                            is Child.Test2Child -> TestContent(
                                child.component,
                                text = "TestContent2"
                            )
                        }
                    }
                }
            }
        }
    }
}

fun DrawerState.closeIn(scope: CoroutineScope) {
    scope.launch {
        this@closeIn.close()
    }
}
