package model

import play.api.libs.json.Json

case class Statistic(sum: Double, average: Option[Double], max: Double, min: Double, count: Long, timeStamp: Option[Long] = null) {
    override def toString(): String =  s"Statistic {sum:$sum, average:$average, " +
      s"max:$max, min:$min, count:$count, ts:$timeStamp}"
}

object Statistic { implicit val format = Json.format[Statistic] }
