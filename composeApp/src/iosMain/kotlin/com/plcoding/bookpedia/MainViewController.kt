package com.plcoding.bookpedia

import androidx.compose.ui.window.ComposeUIViewController
import com.plcoding.bookpedia.di.initCoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initCoin()
    }
) {
    App()
}