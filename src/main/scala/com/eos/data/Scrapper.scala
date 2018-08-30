package com.eos.data

import java.io.File

import play.api.libs.json._

import scala.io.Source

object Scrapper extends App {

  while(true){
    new File("/home/ameet/eosdatachallenge-master/output/raw")
      .listFiles()
      .filter(_.getName == "dump-11")
      .foreach(file => {
//        println(s"processing ${file.getName}")
//        listOfProcessedFiles += file.getName
        val lines = Source.fromFile(file).getLines()
        lines.filter(x => x.contains("HISTORY_TRACES")).take(1).foreach(x => {
          val doc = Json.parse(x.substring(x.indexOf('{'), x.lastIndexOf('}')+1))
          println((doc \ "action_traces").get)
        })

      })

    Thread.sleep(50000)
  }


}