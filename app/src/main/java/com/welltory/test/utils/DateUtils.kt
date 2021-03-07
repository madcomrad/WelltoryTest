package com.welltory.test.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit

fun LocalDateTime.nextHour(): LocalDateTime =
    plusHours(1L).truncatedTo(ChronoUnit.HOURS)

fun LocalDateTime.nextDay(): LocalDateTime =
    plusDays(1L).truncatedTo(ChronoUnit.DAYS)
