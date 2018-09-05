package com.eos.data

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.Document

import collection.JavaConverters._
import scala.util.Try

object DbUtil{

  private var client: MongoClient = _
  private var db: MongoDatabase = _
  private val collection = "newaccount"
  private val host = "mongo"
  private val port = 27017

  def initializeDb(): Unit = {
    client = new MongoClient(host, port)
    db = client.getDatabase("eosio")
    Try(db.createCollection(collection))
  }

  def closeDb(): Unit = client.close()

  def addToDb(li: Seq[Account]) = insert(db, getData(li))

  private def getData(li: Seq[Account]): Seq[Document] =
    li.map(x =>
      new Document("creator", x.creator)
      .append("name", x.name)
    )

  private def insert(db: MongoDatabase, data: Seq[Document]): Unit =
    db.getCollection(collection).insertMany(data.asJava)

}
