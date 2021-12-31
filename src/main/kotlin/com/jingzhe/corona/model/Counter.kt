package com.jingzhe.corona.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.bson.codecs.pojo.annotations.BsonId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Counter (@BsonId val id : String,
                    val counter: Int
)