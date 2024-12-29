package com.tecknobit.pandoro.helpers

import io.ktor.client.HttpClient

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.pandoro.imageLoader] instance
 */
expect fun customHttpClient() : HttpClient