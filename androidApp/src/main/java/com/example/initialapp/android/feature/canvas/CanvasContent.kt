package com.example.initialapp.android.feature.canvas

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import com.example.initialapp.android.feature.canvas.view.dialog.TextDialog
import com.example.initialapp.android.feature.canvas.view.ToolsRow
import com.example.initialapp.android.feature.canvas.view.DrawingCanvas
import com.example.initialapp.feature.canvas.component.CanvasComponent
import com.example.initialapp.feature.canvas.state.CanvasEvent
import com.example.initialapp.feature.canvas.state.Result
import java.io.ByteArrayOutputStream

@Composable
fun CanvasContent(component: CanvasComponent, onNavigationClick: () -> Unit) {
    val state by component.state.collectAsState()

    val localView = LocalView.current
    var coordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val context = LocalContext.current

    fun handleBitmap(event: CanvasEvent.SaveCurrent? = null) {
        try {
            val array = createBitmap(localView, coordinates!!)?.toByteArray()
            if (array != null) {
                component.handleEvent(event?.copy(image = array) ?: CanvasEvent.SaveCurrent(array))
            } else {
                component.handleEvent(CanvasEvent.SaveImageResult(Result.Fail))
            }
        } catch (_: Throwable) {
            component.handleEvent(CanvasEvent.SaveImageResult(Result.Fail))
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            handleBitmap()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ToolsRow(state = state, onNavigationClick = onNavigationClick) {
            when (it) {
                is CanvasEvent.SaveCurrent -> {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        handleBitmap(it)
                    } else {
                        launcher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }

                else -> component.handleEvent(it)
            }
        }
        DrawingCanvas(state = state, modifier = Modifier.onGloballyPositioned {
            coordinates = it
        }, onDraw = component::handleEvent)
    }
    state.saveResult?.let {
        TextDialog(
            text = when (it) {
                Result.Success -> "Image saved"
                Result.Fail -> "Failed to save image"
            }
        ) {
            component.handleEvent(CanvasEvent.DismissDialog)
        }
    }
}



fun createBitmap(view: View, coordinates: LayoutCoordinates): Bitmap? {
    val whole = try {
        view.drawToBitmap(Bitmap.Config.ARGB_8888)
    } catch (e: IllegalStateException) {
        return null
    }
    val bounds = coordinates.boundsInRoot()
    return Bitmap.createBitmap(
        whole,
        bounds.left.toInt(),
        bounds.top.toInt(),
        bounds.width.toInt(),
        bounds.height.toInt()
    )
}

private fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}