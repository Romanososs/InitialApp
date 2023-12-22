package com.example.initialapp.android.feature.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.example.initialapp.android.feature.canvas.CanvasContent
import com.example.initialapp.android.feature.test.TestContent
import com.example.initialapp.feature.root.RootComponent
import com.example.initialapp.feature.root.RootComponent.Child
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RootContent(component: RootComponent) {
    val childStack by component.stack.subscribeAsState()
    val activeComponent = childStack.active.instance

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = activeComponent !is Child.CanvasChild || drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
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
                    is Child.Test1Child -> TestContent(child.component, text = "TestContent1")
                    is Child.Test2Child -> TestContent(child.component, text = "TestContent2")
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
