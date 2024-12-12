package com.tecknobit.pandoro.helpers

import com.tecknobit.equinoxcore.annotations.Wrapper
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@Deprecated(
    message = "TO USE THE ORIGINAL ONE API"
)
object TimeFormatter {

    private var defaultPattern: String = "dd/MM/yyyy HH:mm:ss"

    private var defaultDatePattern: String = "dd/MM/yyyy"

    var invalidTimeGuard: Long = -1

    @Wrapper
    fun Long.formatAsDateString(
        invalidTimeDefValue: String? = null
    ): String {
        return formatAsTimeString(
            invalidTimeDefValue = invalidTimeDefValue,
            pattern = defaultDatePattern
        )
    }

    @Wrapper
    fun Long.formatAsTimeString(
        invalidTimeDefValue: String? = null
    ): String {
        return formatAsTimeString(
            invalidTimeDefValue = invalidTimeDefValue,
            pattern = defaultPattern
        )
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun Long.formatAsTimeString(
        invalidTimeDefValue: String? = null,
        pattern: String
    ): String {
        invalidTimeDefValue?.let { defValue ->
            if(this == invalidTimeGuard)
                return defValue
        }
        val instant = Instant.fromEpochMilliseconds(this)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return LocalDateTime.Format {
            byUnicodePattern(pattern)
        }.format(localDateTime)
    }

    fun Long.daysUntilNow() : Int {
        return daysUntil(
            untilDate = Clock.System.now().toEpochMilliseconds()
        )
    }

    fun Long.daysUntil(
        untilDate: Long
    ) : Int {
        val startDate = Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val endDate = Instant.fromEpochMilliseconds(untilDate)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        return startDate
            .daysUntil(
                other = endDate
            )
    }

}