package com.example.initialapp.feature.canvas.repository

import com.example.initialapp.common.storage.StorageDataSource
import com.example.initialapp.feature.canvas.model.File
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface MediaStorageRepository {
    fun saveImageToStorage(directoryName: String, image: File): Boolean
}

class MediaStorageRepositoryImpl : MediaStorageRepository, KoinComponent {
    private val storageDS: StorageDataSource by inject()

    override fun saveImageToStorage(directoryName: String, image: File): Boolean {
        return storageDS.saveFileToStorage(directoryName, image)
    }

}