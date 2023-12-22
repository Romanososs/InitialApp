package com.example.initialapp.feature.canvas.useCase

import com.example.initialapp.feature.canvas.model.File
import com.example.initialapp.feature.canvas.repository.MediaStorageRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface MediaUseCase {
    fun saveImage(image: ByteArray): Boolean
}

class MediaUseCaseImpl : MediaUseCase, KoinComponent {
    private val mediaStorageRepo: MediaStorageRepository by inject()

    private val directoryName = "InitialAppPreview"

    override fun saveImage(image: ByteArray): Boolean {
        val currentMoment: Instant = Clock.System.now()
        val dateTimeStr = currentMoment.toLocalDateTime(TimeZone.UTC).toString() + ".jpg"
        return mediaStorageRepo.saveImageToStorage(directoryName, File(dateTimeStr, image))
    }
}