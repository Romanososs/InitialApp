package com.example.initialapp.common.storage

import com.example.initialapp.feature.canvas.model.File


expect interface NativeStorageDataSource

interface StorageDataSource: NativeStorageDataSource {
    fun saveFileToStorage(directory: String, file: File): Boolean
}

expect class StorageDataSourceImpl(): StorageDataSource