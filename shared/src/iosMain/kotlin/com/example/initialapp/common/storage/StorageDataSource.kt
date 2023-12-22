package com.example.initialapp.common.storage

import com.example.initialapp.feature.canvas.model.File
import org.koin.core.component.KoinComponent

actual interface NativeStorageDataSource

actual class StorageDataSourceImpl: KoinComponent, StorageDataSource {
    override fun saveFileToStorage(directory: String, file: File): Boolean {
        TODO()
    }
}