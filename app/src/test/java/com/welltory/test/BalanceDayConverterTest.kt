package com.welltory.test

import org.junit.Assert.assertTrue
import org.junit.Test

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

    @Test
    fun aaa() {
        val input = sequenceOf(
                mapOf("startDate" to "2020-04-20 08:54:38", "endDate" to "2020-04-20 08:55:40"),
                mapOf("startDate" to "2020-04-20 09:40:32", "endDate" to "2020-04-20 09:40:34"),
                mapOf("startDate" to "2020-04-20 09:42:29", "endDate" to "2020-04-20 09:42:34"),
                mapOf("startDate" to "2020-04-20 09:44:01", "endDate" to "2020-04-20 09:44:57"),
                mapOf("startDate" to "2020-04-20 09:49:21", "endDate" to "2020-04-20 09:50:07"),
                mapOf("startDate" to "2020-04-20 10:24:06", "endDate" to "2020-04-20 10:24:16"),
                mapOf("startDate" to "2020-04-20 10:31:49", "endDate" to "2020-04-20 10:31:52"),
                mapOf("startDate" to "2020-04-20 10:31:59", "endDate" to "2020-04-20 10:38:56"),
                mapOf("startDate" to "2020-04-20 10:47:38", "endDate" to "2020-04-20 10:47:43"),
                mapOf("startDate" to "2020-04-20 11:41:57", "endDate" to "2020-04-20 11:42:37"),
                mapOf("startDate" to "2020-04-20 11:55:17", "endDate" to "2020-04-20 11:55:30"),
                mapOf("startDate" to "2020-04-20 12:07:29", "endDate" to "2020-04-20 12:07:54"),
                mapOf("startDate" to "2020-04-20 12:12:59", "endDate" to "2020-04-20 12:13:53"),
                mapOf("startDate" to "2020-04-20 12:13:53", "endDate" to "2020-04-20 12:23:53"),
                mapOf("startDate" to "2020-04-20 12:23:58", "endDate" to "2020-04-20 12:24:29"),
                mapOf("startDate" to "2020-04-20 12:25:56", "endDate" to "2020-04-20 12:26:09"),
                mapOf("startDate" to "2020-04-20 12:26:09", "endDate" to "2020-04-20 12:26:11"),
                mapOf("startDate" to "2020-04-20 12:26:11", "endDate" to "2020-04-20 12:26:29"),
                mapOf("startDate" to "2020-04-20 12:27:56", "endDate" to "2020-04-20 12:28:47"),
                mapOf("startDate" to "2020-04-20 12:31:03", "endDate" to "2020-04-20 12:31:41"),
                mapOf("startDate" to "2020-04-20 12:34:40", "endDate" to "2020-04-20 12:34:42"),
                mapOf("startDate" to "2020-04-20 12:37:24", "endDate" to "2020-04-20 12:37:26"),
                mapOf("startDate" to "2020-04-20 12:50:16", "endDate" to "2020-04-20 12:50:26"),
                mapOf("startDate" to "2020-04-20 12:52:37", "endDate" to "2020-04-20 12:52:44"),
                mapOf("startDate" to "2020-04-20 12:56:27", "endDate" to "2020-04-20 12:56:48"),
                mapOf("startDate" to "2020-04-20 13:07:42", "endDate" to "2020-04-20 13:07:45"),
                mapOf("startDate" to "2020-04-20 13:09:01", "endDate" to "2020-04-20 13:09:04"),
                mapOf("startDate" to "2020-04-20 13:10:46", "endDate" to "2020-04-20 13:11:12"),
                mapOf("startDate" to "2020-04-20 13:12:21", "endDate" to "2020-04-20 13:12:38"),
                mapOf("startDate" to "2020-04-20 13:17:48", "endDate" to "2020-04-20 13:18:01"),
                mapOf("startDate" to "2020-04-20 13:29:19", "endDate" to "2020-04-20 13:29:22"),
                mapOf("startDate" to "2020-04-20 13:32:10", "endDate" to "2020-04-20 13:32:36"),
                mapOf("startDate" to "2020-04-20 13:33:09", "endDate" to "2020-04-20 13:39:48"),
                mapOf("startDate" to "2020-04-20 13:47:11", "endDate" to "2020-04-20 13:47:16"),
                mapOf("startDate" to "2020-04-20 13:52:41", "endDate" to "2020-04-20 13:53:32"),
                mapOf("startDate" to "2020-04-20 13:55:30", "endDate" to "2020-04-20 13:55:50"),
                mapOf("startDate" to "2020-04-20 14:03:08", "endDate" to "2020-04-20 14:03:31"),
                mapOf("startDate" to "2020-04-20 14:10:25", "endDate" to "2020-04-20 14:10:35"),
                mapOf("startDate" to "2020-04-20 14:18:28", "endDate" to "2020-04-20 14:18:31"),
                mapOf("startDate" to "2020-04-20 14:20:46", "endDate" to "2020-04-20 14:21:14"),
                mapOf("startDate" to "2020-04-20 14:21:58", "endDate" to "2020-04-20 14:22:00"),
                mapOf("startDate" to "2020-04-20 14:24:57", "endDate" to "2020-04-20 14:25:00"),
                mapOf("startDate" to "2020-04-20 14:26:11", "endDate" to "2020-04-20 14:26:24"),
                mapOf("startDate" to "2020-04-20 14:37:19", "endDate" to "2020-04-20 14:37:24"),
                mapOf("startDate" to "2020-04-20 14:41:42", "endDate" to "2020-04-20 14:41:48"),
                mapOf("startDate" to "2020-04-20 14:49:46", "endDate" to "2020-04-20 14:49:48"),
                mapOf("startDate" to "2020-04-20 15:23:18", "endDate" to "2020-04-20 15:24:16"),
                mapOf("startDate" to "2020-04-20 15:24:24", "endDate" to "2020-04-20 15:25:15"),
                mapOf("startDate" to "2020-04-20 15:25:46", "endDate" to "2020-04-20 15:26:37"),
                mapOf("startDate" to "2020-04-20 15:26:49", "endDate" to "2020-04-20 15:26:59"),
                mapOf("startDate" to "2020-04-20 15:29:12", "endDate" to "2020-04-20 15:29:48"),
                mapOf("startDate" to "2020-04-20 15:30:49", "endDate" to "2020-04-20 15:31:15"),
                mapOf("startDate" to "2020-04-20 15:33:15", "endDate" to "2020-04-20 15:43:15"),
                mapOf("startDate" to "2020-04-20 15:43:15", "endDate" to "2020-04-20 15:43:54"),
                mapOf("startDate" to "2020-04-20 15:44:17", "endDate" to "2020-04-20 15:54:17"),
                mapOf("startDate" to "2020-04-20 15:54:17", "endDate" to "2020-04-20 15:54:53"),
                mapOf("startDate" to "2020-04-20 15:55:21", "endDate" to "2020-04-20 15:55:23"),
                mapOf("startDate" to "2020-04-20 16:01:50", "endDate" to "2020-04-20 16:02:43"),
                mapOf("startDate" to "2020-04-20 16:03:03", "endDate" to "2020-04-20 16:03:39"),
                mapOf("startDate" to "2020-04-20 16:04:17", "endDate" to "2020-04-20 16:04:20"),
                mapOf("startDate" to "2020-04-20 16:13:33", "endDate" to "2020-04-20 16:14:31"),
                mapOf("startDate" to "2020-04-20 16:14:34", "endDate" to "2020-04-20 16:15:15"),
                mapOf("startDate" to "2020-04-20 16:17:02", "endDate" to "2020-04-20 16:18:03"),
                mapOf("startDate" to "2020-04-20 16:18:03", "endDate" to "2020-04-20 16:18:43"),
                mapOf("startDate" to "2020-04-20 16:19:39", "endDate" to "2020-04-20 16:20:10"),
                mapOf("startDate" to "2020-04-20 16:27:53", "endDate" to "2020-04-20 16:33:24"),
                mapOf("startDate" to "2020-04-20 16:42:35", "endDate" to "2020-04-20 16:42:42"),
                mapOf("startDate" to "2020-04-20 16:56:35", "endDate" to "2020-04-20 16:56:57"),
                mapOf("startDate" to "2020-04-20 16:57:43", "endDate" to "2020-04-20 16:57:49"),
                mapOf("startDate" to "2020-04-20 17:09:02", "endDate" to "2020-04-20 17:09:07"),
                mapOf("startDate" to "2020-04-20 17:24:30", "endDate" to "2020-04-20 17:29:14"),
                mapOf("startDate" to "2020-04-20 17:44:46", "endDate" to "2020-04-20 17:44:48"),
                mapOf("startDate" to "2020-04-20 17:50:47", "endDate" to "2020-04-20 17:51:20"),
                mapOf("startDate" to "2020-04-20 18:17:26", "endDate" to "2020-04-20 18:17:59"),
                mapOf("startDate" to "2020-04-20 18:19:05", "endDate" to "2020-04-20 18:20:01"),
                mapOf("startDate" to "2020-04-20 18:29:24", "endDate" to "2020-04-20 18:30:00"),
                mapOf("startDate" to "2020-04-20 18:31:37", "endDate" to "2020-04-20 18:31:40"),
                mapOf("startDate" to "2020-04-20 18:32:49", "endDate" to "2020-04-20 18:32:54"),
                mapOf("startDate" to "2020-04-20 18:35:30", "endDate" to "2020-04-20 18:35:37"),
                mapOf("startDate" to "2020-04-20 18:37:43", "endDate" to "2020-04-20 18:38:23"),
                mapOf("startDate" to "2020-04-20 18:39:09", "endDate" to "2020-04-20 18:39:22"),
                mapOf("startDate" to "2020-04-20 18:40:03", "endDate" to "2020-04-20 18:42:21"),
                mapOf("startDate" to "2020-04-20 18:51:58", "endDate" to "2020-04-20 18:52:01"),
                mapOf("startDate" to "2020-04-20 19:02:38", "endDate" to "2020-04-20 19:06:28"),
                mapOf("startDate" to "2020-04-20 19:15:28", "endDate" to "2020-04-20 19:23:07"),
                mapOf("startDate" to "2020-04-20 19:28:22", "endDate" to "2020-04-20 19:28:24"),
                mapOf("startDate" to "2020-04-20 19:28:24", "endDate" to "2020-04-20 19:28:52"),
                mapOf("startDate" to "2020-04-20 19:35:50", "endDate" to "2020-04-20 19:36:23"),
                mapOf("startDate" to "2020-04-20 19:36:51", "endDate" to "2020-04-20 19:37:29"),
                mapOf("startDate" to "2020-04-20 19:39:06", "endDate" to "2020-04-20 19:39:19"),
                mapOf("startDate" to "2020-04-20 19:41:34", "endDate" to "2020-04-20 19:42:18"),
                mapOf("startDate" to "2020-04-20 19:42:48", "endDate" to "2020-04-20 19:43:45"),
                mapOf("startDate" to "2020-04-20 19:44:28", "endDate" to "2020-04-20 19:44:30"),
                mapOf("startDate" to "2020-04-20 19:48:41", "endDate" to "2020-04-20 19:48:46"),
                mapOf("startDate" to "2020-04-20 19:49:02", "endDate" to "2020-04-20 19:52:39"),
                mapOf("startDate" to "2020-04-20 20:02:30", "endDate" to "2020-04-20 20:03:31"),
                mapOf("startDate" to "2020-04-20 20:03:31", "endDate" to "2020-04-20 20:04:02"),
                mapOf("startDate" to "2020-04-20 20:04:32", "endDate" to "2020-04-20 20:05:10"),
                mapOf("startDate" to "2020-04-20 20:05:36", "endDate" to "2020-04-20 20:06:14"),
                mapOf("startDate" to "2020-04-20 20:06:45", "endDate" to "2020-04-20 20:07:38"),
                mapOf("startDate" to "2020-04-20 20:12:25", "endDate" to "2020-04-20 20:12:45"),
                mapOf("startDate" to "2020-04-20 20:13:36", "endDate" to "2020-04-20 20:13:39"),
                mapOf("startDate" to "2020-04-20 20:15:44", "endDate" to "2020-04-20 20:15:54"),
                mapOf("startDate" to "2020-04-20 20:24:59", "endDate" to "2020-04-20 20:25:02"),
                mapOf("startDate" to "2020-04-20 20:26:08", "endDate" to "2020-04-20 20:26:11"),
                mapOf("startDate" to "2020-04-20 20:27:48", "endDate" to "2020-04-20 20:28:26"),
                mapOf("startDate" to "2020-04-20 20:30:26", "endDate" to "2020-04-20 20:30:29"),
                mapOf("startDate" to "2020-04-20 20:32:54", "endDate" to "2020-04-20 20:32:59"),
                mapOf("startDate" to "2020-04-20 20:34:21", "endDate" to "2020-04-20 20:34:24"),
                mapOf("startDate" to "2020-04-20 20:36:39", "endDate" to "2020-04-20 20:36:42"),
                mapOf("startDate" to "2020-04-20 20:44:25", "endDate" to "2020-04-20 20:45:23"),
                mapOf("startDate" to "2020-04-20 20:45:33", "endDate" to "2020-04-20 20:46:09"),
                mapOf("startDate" to "2020-04-20 20:46:35", "endDate" to "2020-04-20 20:46:37"),
                mapOf("startDate" to "2020-04-20 20:47:51", "endDate" to "2020-04-20 20:48:45"),
                mapOf("startDate" to "2020-04-20 20:50:01", "endDate" to "2020-04-20 20:50:09"),
                mapOf("startDate" to "2020-04-20 20:52:17", "endDate" to "2020-04-20 20:52:24"),
                mapOf("startDate" to "2020-04-20 20:55:21", "endDate" to "2020-04-20 20:55:26"),
                mapOf("startDate" to "2020-04-20 20:59:54", "endDate" to "2020-04-20 21:00:33"),
                mapOf("startDate" to "2020-04-20 21:04:00", "endDate" to "2020-04-20 21:04:43"),
                mapOf("startDate" to "2020-04-20 21:10:15", "endDate" to "2020-04-20 21:10:18"),
                mapOf("startDate" to "2020-04-20 21:11:14", "endDate" to "2020-04-20 21:12:08"),
                mapOf("startDate" to "2020-04-20 21:12:20", "endDate" to "2020-04-20 21:13:06"),
                mapOf("startDate" to "2020-04-20 21:13:52", "endDate" to "2020-04-20 21:13:55"),
                mapOf("startDate" to "2020-04-20 21:16:41", "endDate" to "2020-04-20 21:16:44"),
                mapOf("startDate" to "2020-04-20 21:21:10", "endDate" to "2020-04-20 21:21:18"),
                mapOf("startDate" to "2020-04-20 21:29:03", "endDate" to "2020-04-20 21:29:08"),
                mapOf("startDate" to "2020-04-20 21:29:11", "endDate" to "2020-04-20 21:38:15"),
                mapOf("startDate" to "2020-04-20 21:40:08", "endDate" to "2020-04-20 21:40:46"),
                mapOf("startDate" to "2020-04-20 21:42:23", "endDate" to "2020-04-20 21:43:07"),
                mapOf("startDate" to "2020-04-20 21:46:13", "endDate" to "2020-04-20 21:46:29"),
                mapOf("startDate" to "2020-04-20 21:55:21", "endDate" to "2020-04-20 21:55:28"),
                mapOf("startDate" to "2020-04-20 21:58:09", "endDate" to "2020-04-20 21:58:12"),
                mapOf("startDate" to "2020-04-20 22:14:35", "endDate" to "2020-04-20 22:17:31"),
                mapOf("startDate" to "2020-04-20 22:29:23", "endDate" to "2020-04-20 22:29:25"),
                mapOf("startDate" to "2020-04-20 22:39:37", "endDate" to "2020-04-20 22:39:57"),
                mapOf("startDate" to "2020-04-20 22:41:14", "endDate" to "2020-04-20 22:41:24"),
                mapOf("startDate" to "2020-04-20 22:43:24", "endDate" to "2020-04-20 22:43:29"),
                mapOf("startDate" to "2020-04-20 22:48:00", "endDate" to "2020-04-20 22:48:03"),
                mapOf("startDate" to "2020-04-20 22:56:57", "endDate" to "2020-04-20 22:57:25"),
                mapOf("startDate" to "2020-04-20 22:58:14", "endDate" to "2020-04-20 22:59:13"),
                mapOf("startDate" to "2020-04-20 23:02:01", "endDate" to "2020-04-20 23:02:17"),
                mapOf("startDate" to "2020-04-20 23:08:15", "endDate" to "2020-04-20 23:08:20"),
                mapOf("startDate" to "2020-04-20 23:15:22", "endDate" to "2020-04-20 23:15:29"),
                mapOf("startDate" to "2020-04-20 23:16:38", "endDate" to "2020-04-20 23:17:32"),
                mapOf("startDate" to "2020-04-20 23:17:57", "endDate" to "2020-04-20 23:18:56"),
                mapOf("startDate" to "2020-04-20 23:19:24", "endDate" to "2020-04-20 23:19:55"),
                mapOf("startDate" to "2020-04-20 23:20:41", "endDate" to "2020-04-20 23:21:09"),
                mapOf("startDate" to "2020-04-20 23:22:07", "endDate" to "2020-04-20 23:23:03"),
                mapOf("startDate" to "2020-04-20 23:23:08", "endDate" to "2020-04-20 23:23:52"),
                mapOf("startDate" to "2020-04-20 23:25:21", "endDate" to "2020-04-20 23:34:56"),
                mapOf("startDate" to "2020-04-20 23:36:03", "endDate" to "2020-04-20 23:36:05"),
                mapOf("startDate" to "2020-04-20 23:36:23", "endDate" to "2020-04-20 23:40:41"),
                mapOf("startDate" to "2020-04-20 23:49:00", "endDate" to "2020-04-20 23:58:56"),
        )
//        val input = sequenceOf(
//                mapOf("startDate" to "2020-03-20 00:59:00", "endDate" to "2020-03-20 01:05:00"),
//                mapOf("startDate" to "2020-03-20 01:59:00", "endDate" to "2020-03-20 02:02:00"),
//                mapOf("startDate" to "2020-03-20 02:57:00", "endDate" to "2020-03-20 03:03:00"),
//                mapOf("startDate" to "2020-03-20 05:57:00", "endDate" to "2020-03-20 07:03:00"),
//        )
        val result = BalanceDayConverter.convert(input)
        result.onEach {
            println(it)
        }
//        assertTrue(result.size == 1)
//        assertTrue(result.first().balancePoints == 6)
    }
}
