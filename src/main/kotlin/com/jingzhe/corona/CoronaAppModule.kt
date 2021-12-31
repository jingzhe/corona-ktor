package com.jingzhe.corona

import com.jingzhe.corona.service.CoronaService
import com.jingzhe.corona.service.CounterService
import com.jingzhe.corona.service.FeedbackService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val coronaAppModule = module(createdAtStart = true) {
    single { KMongo.createClient("YOUR_MONGO_DB_URL").coroutine }
    single { HttpClient(CIO) }
    single { CounterService(get()) }
    single { FeedbackService(get()) }
    single { CoronaService(get(), get(), get()) }
}
