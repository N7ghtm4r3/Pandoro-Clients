package com.tecknobit.pandoro

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.tecknobit.ametistaengine.AmetistaEngine
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // AmetistaEngine.intake()
    ComposeViewport(document.body!!) {
        App()
    }
}