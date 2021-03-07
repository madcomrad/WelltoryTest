package com.welltory.test.model

import org.threeten.bp.LocalDate

data class BalanceDay(
    val day: LocalDate,
    val balancePoints: Int,
    val balanceDay: Int,
)
