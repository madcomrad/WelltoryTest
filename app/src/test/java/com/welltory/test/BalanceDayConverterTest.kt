package com.welltory.test

import org.junit.Test

import org.junit.Assert.*

class BalanceDayConverterTest {

    @Test
    fun one_balance_point_for_one_hour() {
        val input = sequenceOf(
            mapOf("startDate" to "2020-03-20 00:04:00", "endDate" to "2020-03-20 00:08:00"),
            mapOf("startDate" to "2020-03-20 00:09:00", "endDate" to "2020-03-20 00:16:00"),
            mapOf("startDate" to "2020-03-20 00:20:00", "endDate" to "2020-03-20 00:26:00"),
        )
        val result = BalanceDayConverter.convert(input)
        result.onEach {
            println(it)
        }
        assertTrue(result.first().balancePoints == 1)
    }

    @Test
    fun two_balance_points_for_one_hour() {
        val input = sequenceOf(
            mapOf("startDate" to "2020-03-20 00:04:00", "endDate" to "2020-03-20 00:08:00"),
            mapOf("startDate" to "2020-03-20 00:09:00", "endDate" to "2020-03-20 00:16:00"),
            mapOf("startDate" to "2020-03-20 00:20:00", "endDate" to "2020-03-20 00:26:00"),
            mapOf("startDate" to "2020-03-20 00:50:00", "endDate" to "2020-03-20 00:56:00"),
        )
        val result = BalanceDayConverter.convert(input)
        result.onEach {
            println(it)
        }
        assertTrue(result.first().balancePoints == 2)
    }

    @Test
    fun negligible_activity_interval() {
        val input = sequenceOf(
            mapOf("startDate" to "2020-03-20 00:04:00", "endDate" to "2020-03-20 00:05:00"),
            mapOf("startDate" to "2020-03-20 00:05:10", "endDate" to "2020-03-20 00:05:59"),
            mapOf("startDate" to "2020-03-20 00:06:10", "endDate" to "2020-03-20 00:07:00"),
        )
        val result = BalanceDayConverter.convert(input)
        result.onEach {
            println(it)
        }
        assertTrue(result.first().balancePoints == 1)
    }

    @Test
    fun split_by_astronomical_hour_activity() {
        val input = sequenceOf(
            mapOf("startDate" to "2020-03-20 00:59:00", "endDate" to "2020-03-20 01:05:00"),
            mapOf("startDate" to "2020-03-20 01:59:00", "endDate" to "2020-03-20 02:02:00"),
            mapOf("startDate" to "2020-03-20 02:57:00", "endDate" to "2020-03-20 03:03:00"),
            mapOf("startDate" to "2020-03-20 05:57:00", "endDate" to "2020-03-20 07:03:00"),
        )
        val result = BalanceDayConverter.convert(input)
        result.onEach {
            println(it)
        }
        assertTrue(result.size == 1)
        assertTrue(result.first().balancePoints == 6)
    }

    @Test
    fun split_by_date_activity() {
        val input = sequenceOf(
            mapOf("startDate" to "2020-03-20 23:59:00", "endDate" to "2020-03-21 00:05:00"),
            mapOf("startDate" to "2020-03-21 23:59:00", "endDate" to "2020-03-22 00:02:00"),
            mapOf("startDate" to "2020-03-23 23:57:00", "endDate" to "2020-03-24 00:03:00"),
            mapOf("startDate" to "2020-03-25 23:57:00", "endDate" to "2020-03-27 00:03:00"),
        )
        val result = BalanceDayConverter.convert(input)
        result.onEach {
            println(it)
        }
        assertTrue(result.size == 6)
        assertTrue(
            result[0].balancePoints == 1 &&
            result[1].balancePoints == 1 &&
            result[2].balancePoints == 1 &&
            result[3].balancePoints == 1 &&
            result[4].balancePoints == 24 &&
            result[5].balancePoints == 1
        )
    }
}
