import scala.collection.mutable.{ListBuffer, Map}

class Transaction(val ccy: String, var amount: Int)

class CurrencyBalanceCalculator {

  var transactionsMap = Map[String, Transaction]()
  var ccyBalances = new ListBuffer[Transaction]()

  def executeTransactions(ccy: String, amount: Int) = {
    if (transactionsMap.contains(ccy)) {
      var t = transactionsMap(ccy)
      ccyBalances = ccyBalances.filter(_ != t)
      t.amount += amount
      ccyBalances += t
    } else {
      val t = new Transaction(ccy, amount)
      ccyBalances += t
      transactionsMap(ccy) = t
    }
  }

  def processInput(input: String): Option[(String, Int)] = {
    val splitted = input.split(" ")
    if(splitted.length == 2) {
      if(splitted(0).toIntOption.isDefined) {
        Some(splitted(1), splitted(0).toInt)
      }
      else if(splitted(1).toIntOption.isDefined) {
        Some(splitted(0), splitted(1).toInt)
      }
      else None
    }
    else None
  }

}




