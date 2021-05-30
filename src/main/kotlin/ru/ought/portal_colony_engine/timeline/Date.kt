package ru.ought.portal_colony_engine.timeline

import java.time.LocalDate
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