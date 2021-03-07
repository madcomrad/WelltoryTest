package com.welltory.test.model

import org.threeten.bp.LocalDateTime

data class Activity(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val continuousActivity: Long,
)
