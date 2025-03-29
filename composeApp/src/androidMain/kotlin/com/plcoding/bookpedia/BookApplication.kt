package com.plcoding.bookpedia

import android.app.Application
import com.plcoding.bookpedia.di.initCoin
import org.koin.android.ext.koin.androidContext

class BookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initCoin {
            androidContext(this@BookApplication)
        }

    }
}