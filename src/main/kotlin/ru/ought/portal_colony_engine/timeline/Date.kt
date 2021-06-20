package ru.ought.portal_colony_engine.timeline

import java.time.LocalDate
import java.time.Month
import java.time.Month.*
import java.time.temporal.ChronoUnit

val ZERO_DAY: LocalDate = LocalDate.of(1889, 12, 31)

fun Int.toDate(): LocalDate {
    require(this > 0)
    return ZERO_DAY.plusDays(this.toLong())
}

fun LocalDate.toDay(): Int {
    require(this > ZERO_DAY)
    return ZERO_DAY.until(this, ChronoUnit.DAYS).toInt()
}

enum class Season(val months: List<Month>) {
    WINTER(listOf(DECEMBER, JANUARY, FEBRUARY)),
    SPRING(listOf(MARCH, APRIL, MAY)),
    SUMMER(listOf(JUNE, JULY, AUGUST)),
    AUTUMN(listOf(SEPTEMBER, OCTOBER, NOVEMBER)),
}

fun LocalDate.getSeason(): Season {
    return Season.values().find { it.months.contains(month) }!!
}

fun Int.getSeason(): Season {
    return toDate().getSeason()
}