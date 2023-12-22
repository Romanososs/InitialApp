package com.example.initialapp.common.storage

import android.app.Application
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.FileOutputStream


actual interface NativeStorageDataSource

actual class StorageDataSourceImpl : KoinComponent, StorageDataSource {
    private val context: Application by inject()

    override fun saveFileToStorage(
        directory: String,
        file: com.example.initialapp.feature.canvas.model.File
    ): Boolean {
        //TODO проверить работу на 13 андроиде
        val root: File = Environment.getExternalStorageDirectory()
        val androidFile = File(root.absolutePath + "/$directory/${file.name}")
        if (!androidFile.exists()) {
            androidFile.parentFile?.mkdirs()
        }
        return try {
            androidFile.createNewFile()
            FileOutputStream(androidFile).use {
                it.write(file.data)
            }
            callScan(androidFile)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun callScan(file: File) {
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null) { _, _ -> }
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    }
}