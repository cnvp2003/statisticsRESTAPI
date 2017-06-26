package services

import model.{Statistic, Transaction}
import utils.DateUtils
import java.util.concurrent.ConcurrentHashMap

import play.api.Logger

trait StatisticService {

  def processTransaction(transaction: Transaction)

  def getStatistic(): Statistic
}

class StatisticServiceImpl extends StatisticService {
  private val logger = Logger(getClass)
  val statistics = new ConcurrentHashMap[Int, Statistic](60)

  private[services] def findByKey(key: Int): Option[Statistic] = {
    Option(statistics.get(key))
  }

  private[services] def updateStatistic(second: Int, statistic: Statistic) {
    statistics.put(second, statistic)
  }

  def processTransaction(transaction: Transaction): Unit = {
    logger.info(s"Processing Transaction in Service")
    if (transaction != null && Option(transaction.amount).isDefined && Option(transaction.timestamp).isDefined) {
      val tsp: Long = transaction.timestamp
      val amount: Double = transaction.amount
      val second: Int = DateUtils.getSecond(tsp)
      val value = findByKey(second)

     if(value.isDefined){
       value.map { statistic =>
         val newStatistic = if (DateUtils.isInLastMin(statistic.timeStamp.get)){
           statistic.copy(count = statistic.count+1,
             sum = statistic.sum+amount, min = if(statistic.min > amount) amount else statistic.min,
             max = if(statistic.max < amount) amount else statistic.max, timeStamp = Option(tsp))
         }else{
           statistic.copy(count = 1, sum = amount, min = amount, max = amount, timeStamp = Option(tsp))
         }
         updateStatistic(second, newStatistic)
       }
     }else{
       val newStatistic = Statistic(count = 1, sum = amount, min = amount, max = amount, timeStamp = Option(tsp), average = None)
       updateStatistic(second, newStatistic)
     }

    }
  }

  def getStatistic(): Statistic = {
    logger.info(s"Getting Statistics")
    var sum: Double = 0d
    var max: Double = Double.MaxValue
    var min: Double = Double.MinValue
    var count: Long = 0
    import scala.collection.JavaConversions._
    for (statistic <- statistics.values) {
      if (statistic != null && statistic.count > 0 && DateUtils.isInLastMin(statistic.timeStamp.getOrElse(0L))) {
        sum += statistic.sum
        count += statistic.count
        if (max < statistic.max) max = statistic.max
        if (min > statistic.min) min = statistic.min
      }
    }
    val maximum = if (max == Double.MaxValue) 0 else max
    val minimum = if (min == Double.MinValue) 0 else min
    val average = if (count > 0) sum / count else 0
    Statistic(sum, Option(average), maximum, minimum, count)
  }

}