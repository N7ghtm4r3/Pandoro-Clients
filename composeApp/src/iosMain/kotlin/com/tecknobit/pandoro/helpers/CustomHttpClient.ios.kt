package com.tecknobit.pandoro.helpers

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.ametista.imageLoader] instance
 */
actual fun customHttpClient(): HttpClient {
    return HttpClient(CIO) {
        engine {
            https {
                serverName = null
            }
        }
    }
}