package model

import play.api.libs.json.Json

case class Transaction(amount: Double, timestamp: Long)

object Transaction {implicit val format = Json.format[Transaction]}

