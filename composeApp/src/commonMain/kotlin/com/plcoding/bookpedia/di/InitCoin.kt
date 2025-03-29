package com.plcoding.bookpedia.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initCoin(config: KoinAppDeclaration = {}) {
    startKoin(config).modules(sharedModule, platformModule)
}