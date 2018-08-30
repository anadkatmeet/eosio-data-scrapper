package com.eos.data

import com.mongodb.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.Document

import scala.util.Try

object DbUtil {

  private var client: MongoClient = _
  private val db = client.getDatabase("foobar")
  private val collection = "first"
  db.createCollection("first")
  insert(db, getData())
  sys.addShutdownHook(client.close())

  private def setupDb(): Try[Unit] = Try{client = new MongoClient("localhost", 27017)}

  private def getData(): Document =
    new Document("name", "Meet")
      .append("lname", "Anadkat")
      .append("pkey", 1)

  private def insert(db: MongoDatabase, document: Document): Unit =
    db.getCollection(collection).insertOne(document)

}
