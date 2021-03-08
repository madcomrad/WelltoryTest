package com.welltory.test

import android.util.Log
import com.welltory.test.model.Activity
import com.welltory.test.model.BalanceDay
import com.welltory.test.model.IteratorState
import com.welltory.test.utils.addAt
import com.welltory.test.utils.nextDay
import com.welltory.test.utils.nextHour
import com.welltory.test.utils.setAt
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object BalanceDayConverter {

    private const val NEGLIGIBLE_INACTIVITY_PERIOD = 18L
    private const val THREE_MINUTES = 180L
    private const val WORTH_ANOTHER_BALANCE_POINT_PERIOD = 2280L

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")

    fun convert(rows: Sequence<Map<String, String>>): List<BalanceDay> {
        val activityData = mutableMapOf<LocalDate, MutableMap<Int, Int>>()

        var iteratorState: IteratorState? = null
        rows.forEach { row ->
            val startDate = row["startDate"].convertToLocalDateTime()
            val endDate = row["endDate"].convertToLocalDateTime()

            when (startDate.dayOfYear) {
                // same date
                endDate.dayOfYear -> when (startDate.hour) {
                    // same hour
                    endDate.hour -> iteratorState = when (val state = iteratorState) {
                        null -> createNewIteratorState(startDate, endDate)
                        else -> when {
                            state.isSameHour(startDate) -> updateIteratorState(state, startDate, endDate)
                            else -> {
                                state.addBalancePoints(activityData)
                                createNewIteratorState(startDate, endDate)
                            }
                        }
                    }
                    // different hours
                    else -> {
                        var nextHour = startDate.nextHour()
                        val continuousActivity = Duration.between(startDate, nextHour).seconds

                        if (iteratorState?.isSameHour(startDate) != true && continuousActivity >= THREE_MINUTES) {
                            activityData.setBalancePoints(startDate.toLocalDate(), startDate.hour)
                        }

                        while (nextHour.hour != endDate.hour) {
                            activityData.setBalancePoints(nextHour.toLocalDate(), nextHour.hour)
                            nextHour = nextHour.nextHour()
                        }

                        iteratorState = when (val state = iteratorState) {
                            null -> createNewIteratorState(nextHour, endDate)
                            else -> when {
                                state.isSameHour(startDate) -> {
                                    updateIteratorState(state, startDate, startDate.nextHour()).addBalancePoints(activityData)
                                }
                                else -> state.addBalancePoints(activityData)
                            }.let {
                                createNewIteratorState(nextHour, endDate)
                            }
                        }
                    }
                }
                // different dates
                else -> {
                    var nextDay = startDate.nextDay()
                    var nextHour = startDate.nextHour()
                    val continuousActivity = Duration.between(startDate, nextHour).seconds

                    if (iteratorState?.isSameHour(startDate) != true && continuousActivity >= THREE_MINUTES) {
                        activityData.setBalancePoints(startDate.toLocalDate(), startDate.hour)
                    }

                    while (nextHour.dayOfYear != nextDay.dayOfYear) {
                        activityData.setBalancePoints(nextHour.toLocalDate(), nextHour.hour)
                        nextHour = nextHour.nextHour()
                    }

                    while (nextDay.dayOfYear != endDate.dayOfYear) {
                        activityData.fillBalancePointsForFullDay(nextDay.toLocalDate())
                        nextDay = nextDay.nextDay()
                    }

                    nextHour = nextDay
                    while (nextHour.hour != endDate.hour) {
                        activityData.setBalancePoints(nextHour.toLocalDate(), nextHour.hour)
                        nextHour = nextHour.nextHour()
                    }

                    iteratorState = when (val state = iteratorState) {
                        null -> createNewIteratorState(nextHour, endDate)
                        else -> when {
                            state.isSameHour(startDate) -> {
                                updateIteratorState(state, startDate, startDate.nextHour()).addBalancePoints(activityData)
                            }
                            else -> state.addBalancePoints(activityData)
                        }.let {
                            createNewIteratorState(nextHour, endDate)
                        }
                    }
                }
            }
        }
        iteratorState?.addBalancePoints(activityData)

        return activityData.calculateBalanceDay().onEach {
//            Log.d("Balance Day", "day=${it.day}, points=${it.balancePoints}, balanceDay=${it.balanceDay}")
        }
    }


    private fun createNewIteratorState(startDate: LocalDateTime, endDate: LocalDateTime): IteratorState {
        return IteratorState(
            activities = listOf(
                Activity(
                    startDate = startDate,
                    endDate = endDate,
                    continuousActivity = Duration.between(startDate, endDate).seconds
                )
            ),
            currentHour = startDate.hour,
            currentDate = startDate.toLocalDate()
        )
    }

    private fun updateIteratorState(state: IteratorState, startDate: LocalDateTime, endDate: LocalDateTime): IteratorState {
        return when (Duration.between(state.activities.last().endDate, startDate).seconds <= NEGLIGIBLE_INACTIVITY_PERIOD) {
            true -> {
                val extendedActivity = state.activities.last().run {
                    copy(
                        endDate = endDate,
                        continuousActivity = Duration.between(this.startDate, endDate).seconds
                    )
                }
                state.copy(activities = state.activities.setAt(extendedActivity))
            }
            false -> {
                val newActivity = Activity(
                    startDate = startDate,
                    endDate = endDate,
                    continuousActivity = Duration.between(startDate, endDate).seconds
                )
                state.copy(activities = state.activities.addAt(newActivity))
            }
        }
    }

    private fun IteratorState.addBalancePoints(activityData: MutableMap<LocalDate, MutableMap<Int, Int>>) {
        val longEnoughActivities = activities.filter { it.continuousActivity >= THREE_MINUTES }
        when {
            longEnoughActivities.size > 1 -> {
                val balancePoints = when {
                    Duration.between(
                        longEnoughActivities.first().startDate,
                        longEnoughActivities.last().startDate
                    ).seconds >= WORTH_ANOTHER_BALANCE_POINT_PERIOD -> 2
                    else -> 1
                }
                activityData.setBalancePoints(currentDate, currentHour, balancePoints)
            }
            longEnoughActivities.size == 1 -> activityData.setBalancePoints(currentDate, currentHour)
        }
    }

    private fun IteratorState.isSameHour(date: LocalDateTime): Boolean {
        return currentDate == date.toLocalDate() && currentHour == date.hour
    }

    private fun MutableMap<LocalDate, MutableMap<Int, Int>>.setBalancePoints(date: LocalDate, hour: Int, points: Int = 1) {
        get(date)?.let { hourBalancePoints ->
            hourBalancePoints[hour] = points
        } ?: put(date, mutableMapOf(hour to points))
    }

    private fun MutableMap<LocalDate, MutableMap<Int, Int>>.fillBalancePointsForFullDay(date: LocalDate) {
        put(date, mutableMapOf<Int, Int>().apply {
            repeat(24) { hour -> put(hour, 1) }
        })
    }

    private fun MutableMap<LocalDate, MutableMap<Int, Int>>.calculateBalanceDay(): List<BalanceDay> {
        return entries.map { (date, hoursBalancePoints) ->
            val points = hoursBalancePoints.values.sum()
            BalanceDay(
                day = date,
                balancePoints = points,
                balanceDay = points * 10
            )
        }.sortedBy { it.day }
    }

    private fun String?.convertToLocalDateTime(): LocalDateTime = this?.let { date ->
        LocalDateTime.parse(date, dateTimeFormatter)
    } ?: throw RuntimeException("wrong csv data format")

}
