package utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object DateUtils {

    def isInLastMin(timeStamp: Long) = {
        val date = Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val oneMinBefore = LocalDateTime.now().minus(60, ChronoUnit.SECONDS)
        date.isAfter(oneMinBefore)
    }

    def getSecond(timeStamp: Long): Int = {
        val date = Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
        date.getSecond()
    }

}
