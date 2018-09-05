package com.eos.data

import java.io.File

import com.eos.data.DbUtil.{addToDb, closeDb, initializeDb}
import org.apache.commons.io.FileUtils
import play.api.libs.json._

import scala.io.Source

object Scrapper extends App {

  println("Initializing Data Scrapper App")

  initializeDb()
  sys.addShutdownHook(closeDb())

  while(true){
    println(s"Looping list")
    new File("/output/raw").listFiles()
      .filter(_.getName.startsWith("dump-"))
      .foreach(file => {
        println(s"processing ${file.getName}")
        Source.fromFile(file)
          .getLines()
          .filter(x => x.contains("HISTORY_TRACES")).toList.foreach(x => {
          val doc = Json.parse(x.substring(x.indexOf('{'), x.lastIndexOf('}')+1))
          val actSeq = doc \ "action_traces" \\ "act"
          val li = actSeq
            .map(x => OptionalAccount(
              (x \ "account").toOption,
              (x \ "name").toOption,
              (x \ "data" \ "creator").toOption,
              (x \ "data" \ "name").toOption)
            )
            .filter(_.isValid)
            .map(x => Account(x.account.get.toString(), x.accountType.get.toString(), x.creator.get.toString(), x.name.get.toString()))

          println(s"Total new accounts in file ${file.getName} are ${li.size}")
          if(li.nonEmpty) addToDb(li)

        })
        FileUtils.moveFile(file, new File(s"/output/processed/${file.getName}"))
      })
    Thread.sleep(10000)
  }

}

case class OptionalAccount(account: Option[JsValue], accountType: Option[JsValue], creator: Option[JsValue], name: Option[JsValue]){
  def isValid: Boolean = account.isDefined && accountType.isDefined && creator.isDefined && name.isDefined
}

case class Account(account: String, accountType: String, creator: String, name: String)