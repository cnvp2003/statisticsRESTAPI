package controllers

import javax.inject.Inject

import com.google.inject.Singleton
import model.Transaction
import play.api.Logger
import play.api.mvc.{Action, Controller}
import services.StatisticService

@Singleton
class StatisticController @Inject() (statisticService: StatisticService) extends Controller {

    private val logger = Logger(getClass)

    def getStatistics = Action {
        logger.info(s"getting Statistics")
        Ok(views.html.index(s"${statisticService.getStatistic()}"))
    }

    def processTransaction = Action(parse.json) { request =>
        logger.info(s"Adding new Transaction")
        val transaction: Option[Transaction] = (request.body).asOpt[Transaction].map { jsonRequest =>
            statisticService.processTransaction(jsonRequest)
            jsonRequest
        }
        Ok(views.html.index(s"${transaction.get}"))
    }
}
