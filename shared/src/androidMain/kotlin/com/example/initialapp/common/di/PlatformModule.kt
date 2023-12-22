package com.example.initialapp.common.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.initialapp.Database
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module{
    single {
        val driver = AndroidSqliteDriver(Database.Schema, get(), "Database.db")
        Database(driver)
    }
//    single<HttpClientEngine> {
//        Android.create {}
//    }
}