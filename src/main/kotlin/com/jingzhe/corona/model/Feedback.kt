package com.jingzhe.corona.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant
import java.util.UUID.randomUUID
import kotlinx.serialization.Serializable

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Serializable
data class Feedback (@BsonId val id: String = randomUUID().toString(),
                     val name: String = "Unknown",
                     val subject: String,
                     val content: String) {
    val timeStamp: Long = Instant.now().epochSecond
}
