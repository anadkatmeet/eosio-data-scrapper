package com.eos.data

import java.io.File

import com.eos.data.DbUtil.{addToDb, closeDb, initializeDb}
import org.apache.commons.io.FileUtils
import play.api.libs.json._

import scala.io.Source

object Scrapper extends App {

  println("Initializing Data Scrapper App")
  private val inputPath = "/output/raw"
  private val outputPath = "/output/processed"

  initializeDb()
  sys.addShutdownHook(closeDb())

  while(true){
    println(s"Checking for new files...")
    new File(inputPath).listFiles()
      .filter(_.getName.startsWith("dump-"))
      .foreach(file => {
        println(s"processing ${file.getName}")

        val validNewAccounts = Source.fromFile(file).getLines()
          .filter(validEvent)
          .map(x => Json.parse(extractJsString(x)))
          .flatMap(x => extractActionTraces(x))
          .map(x => extractOptionalAcountInfo(x))
          .filter(_.isValid)
          .map(extractNewAcountInfo)
          .toList

        println(s"New accounts found in file ${file.getName} are ${validNewAccounts.size}")
        if(validNewAccounts.nonEmpty) addToDb(validNewAccounts)

        FileUtils.moveFile(file, new File(s"$outputPath/${file.getName}"))
      })
    Thread.sleep(10000)
  }

  private def validEvent(event: String): Boolean = event.contains("HISTORY_TRACES")

  private def extractJsString(event: String) = event.substring(event.indexOf('{'), event.lastIndexOf('}')+1)

  private def extractActionTraces(event: JsValue): Seq[JsValue] = event \ "action_traces" \\ "act"

  private def extractOptionalAcountInfo(x: JsValue): OptionalAccount =
    OptionalAccount(
      (x \ "account").toOption,
      (x \ "name").toOption,
      (x \ "data" \ "creator").toOption,
      (x \ "data" \ "name").toOption
    )

  private def extractNewAcountInfo(x: OptionalAccount): Account =
    Account(
      x.account.get.as[String],
      x.accountType.get.as[String],
      x.creator.get.as[String],
      x.name.get.as[String]
    )

}

case class OptionalAccount(account: Option[JsValue], accountType: Option[JsValue], creator: Option[JsValue], name: Option[JsValue]){
  def isValid: Boolean = account.isDefined && accountType.isDefined && creator.isDefined && name.isDefined
}

case class Account(account: String, accountType: String, creator: String, name: String)