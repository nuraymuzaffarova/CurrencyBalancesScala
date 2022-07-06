import java.io.FileNotFoundException
import java.util.TimerTask
import java.util.concurrent.{Executors, TimeUnit}
import java.io.IOException

object CurrencyBalanceCalculatorApp extends App {

    val ccyBalanceCalc = new CurrencyBalanceCalculator()

    class PublishTask extends TimerTask {
      @Override def run() = {
        ccyBalanceCalc.ccyBalances.filter(_.amount != 0).map(b => println(b.ccy ++ " " ++ b.amount.toString))
      }
    }

    val executor = Executors.newScheduledThreadPool(1)
    executor.scheduleAtFixedRate(new PublishTask, 0, 60, TimeUnit.SECONDS)
    if (args.length == 0) {
      while (true) {
        val userInput = scala.io.StdIn.readLine()
        if (userInput.equals("quit")) {
          System.exit(0)
          executor.shutdown()
        }
        else if (ccyBalanceCalc.processInput(userInput).isDefined) {
            ccyBalanceCalc.processInput(userInput) match {
            case Some((ccy, amount)) => ccyBalanceCalc.executeTransactions(ccy, amount)
            case None => println("Invalid Input")
          }
        }
        else println("Invalid Input")
      }
    }
    else {
      val fileReader = scala.io.Source.fromFile(args(0))
      try {
        for (line <- fileReader.getLines) {
            ccyBalanceCalc.processInput(line) match {
              case Some((ccy, amount)) => ccyBalanceCalc.executeTransactions(ccy, amount)
              case None => println("Invalid Input")
            }
          }
      }
      catch {
         case e: FileNotFoundException => println("Couldn't find that file.")
         case e: IOException           => println("Got an IOException!")
         }
      finally fileReader.close()
      }
  }

