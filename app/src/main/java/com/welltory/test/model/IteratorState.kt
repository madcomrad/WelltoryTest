package com.welltory.test.model

import org.threeten.bp.LocalDate

data class IteratorState(
    val activities: List<Activity>,
    val currentHour: Int,
    val currentDate: LocalDate,
)
