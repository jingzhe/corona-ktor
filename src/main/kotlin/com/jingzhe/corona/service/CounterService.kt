package com.jingzhe.corona.service

import com.jingzhe.corona.dbName
import com.jingzhe.corona.model.Counter
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import org.bson.Document
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.coroutine.CoroutineClient


class CounterService(private val dbClient: CoroutineClient) {

    suspend fun updateCounter(): Counter {
        return dbClient.getDatabase(dbName)
            .getCollection<Counter>("counter")
            .findOneAndUpdate(
                Document("_id", "1"),
                Document("${MongoOperator.inc}", Document("counter", 1)),
                FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER))
            ?: createCounter()
    }

    suspend fun getCounter(): Counter {
        return dbClient.getDatabase(dbName)
            .getCollection<Counter>("counter")
            .findOne(Document("_id", "1"))
            ?: createCounter()
    }

    private suspend fun createCounter(): Counter {
        val first = Counter(
            id = "1",
            counter = 1
        )
        dbClient.getDatabase(dbName)
            .getCollection<Counter>("counter")
            .insertOne(first)

        return first
    }
}