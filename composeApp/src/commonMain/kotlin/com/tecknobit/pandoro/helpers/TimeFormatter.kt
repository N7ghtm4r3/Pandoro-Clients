package com.tecknobit.pandoro.helpers

import com.tecknobit.equinoxcore.annotations.Wrapper
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@Deprecated(
    message = "TO USE THE ORIGINAL ONE API"
)
object TimeFormatter {

    private var defaultPattern: String = "dd/MM/yyyy HH:mm:ss"

    private var defaultDatePattern: String = "dd/MM/yyyy"

    @Wrapper
    fun Long.formatAsDateString(): String {
        return formatAsTimeString(
            pattern = defaultDatePattern
        )
    }

    @Wrapper
    fun Long.formatAsTimeString(): String {
        return formatAsTimeString(
            pattern = defaultPattern
        )
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun Long.formatAsTimeString(
        pattern: String
    ): String {
        val instant = Instant.fromEpochMilliseconds(this)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return LocalDateTime.Format {
            byUnicodePattern(pattern)
        }.format(localDateTime)
    }

}