package init

import com.google.inject.{Binder, Module}
import services.{StatisticService, StatisticServiceImpl}

/**
 * The Guice-Module used in Production
 */
class ProductionModule extends Module {
  def configure(binder: Binder): Unit = {
    binder.bind(classOf[StatisticService]).to(classOf[StatisticServiceImpl])
  }
}
