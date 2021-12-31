package com.jingzhe.corona.service

import com.jingzhe.corona.dbName
import com.jingzhe.corona.model.Feedback
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.flow.Flow
import org.litote.kmongo.coroutine.CoroutineClient

class FeedbackService(private val dbClient: CoroutineClient) {

    fun getFeedback(): Flow<Feedback> {
         return dbClient.getDatabase(dbName)
            .getCollection<Feedback>("feedback")
            .find()
            .sort(Sorts.descending(listOf("time_stamp")))
            .toFlow()
    }

    suspend fun addFeedback(feedback: Feedback): Flow<Feedback> {
        val feedbackCollection = dbClient.getDatabase(dbName)
            .getCollection<Feedback>("feedback")

        feedbackCollection.insertOne(feedback)
        return feedbackCollection.find()
            .sort(Sorts.descending(listOf("time_stamp")))
            .toFlow()
    }
}