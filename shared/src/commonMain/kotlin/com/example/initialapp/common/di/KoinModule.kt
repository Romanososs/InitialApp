package com.example.initialapp.common.di

import com.example.initialapp.common.storage.DBDataSource
import com.example.initialapp.common.storage.DBDataSourceImpl
import com.example.initialapp.common.storage.SettingsDataSource
import com.example.initialapp.common.storage.SettingsDataSourceImpl
import com.example.initialapp.feature.canvas.repository.MediaStorageRepository
import com.example.initialapp.feature.canvas.repository.MediaStorageRepositoryImpl
import com.example.initialapp.common.storage.StorageDataSource
import com.example.initialapp.common.storage.StorageDataSourceImpl
import com.example.initialapp.feature.canvas.repository.CanvasRepository
import com.example.initialapp.feature.canvas.repository.CanvasRepositoryImpl
import com.example.initialapp.feature.canvas.useCase.MediaUseCase
import com.example.initialapp.feature.canvas.useCase.MediaUseCaseImpl
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            commonModule(),
            platformModule()
        )
    }

// called by iOS
fun initKoin() = initKoin {}

fun commonModule() = module {
    single<MediaUseCase> { MediaUseCaseImpl() }

    single<MediaStorageRepository> { MediaStorageRepositoryImpl() }
    single<CanvasRepository> { CanvasRepositoryImpl() }

    single<StorageDataSource> { StorageDataSourceImpl() }
    single<DBDataSource> { DBDataSourceImpl() }
    single<SettingsDataSource> { SettingsDataSourceImpl() }

    single<Json> { Json }
    single { Settings() }

}