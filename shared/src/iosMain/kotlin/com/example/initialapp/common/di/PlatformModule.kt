package com.example.initialapp.common.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.initialapp.Database
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single {
        val driver = NativeSqliteDriver(Database.Schema, "Database.db")
        Database(driver)
    }
//    single<HttpClientEngine> {
//        Darwin.create { }
//    }
}