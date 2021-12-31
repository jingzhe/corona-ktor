package com.jingzhe.corona.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.bson.codecs.pojo.annotations.BsonId

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Snapshot (@BsonId val id : String,
                     val data: Map<String, Int>
)