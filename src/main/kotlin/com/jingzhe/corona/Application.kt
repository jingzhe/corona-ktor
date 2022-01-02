package com.jingzhe.corona

import com.fasterxml.jackson.databind.SerializationFeature
import com.jingzhe.corona.model.Feedback
import com.jingzhe.corona.service.CoronaService
import com.jingzhe.corona.service.CounterService
import com.jingzhe.corona.service.FeedbackService
import com.jingzhe.corona.utils.CoronaUtils
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.jackson
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.toList
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.BadRequest)
        }
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(Koin) {
        modules(coronaAppModule)
    }

    install(DefaultHeaders)
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.AccessControlAllowOrigin)
        anyHost()
    }

    // Lazy inject
    val counterService by inject<CounterService>()
    val feedbackService by inject<FeedbackService>()
    val coronaService by inject<CoronaService>()

    routing {
        get("/snapshot") {
            call.respond(coronaService.getSnapshotData())
        }

        get("/old-snapshot") {
            val date = call.request.queryParameters["date"] ?: throw Exception("date parameter is needed")
            call.respond(coronaService.getOldSnapshotData(date))
        }

        get("/fetch-snapshot") {
            val todayDate = CoronaUtils.getTodayDate()
            call.respond(coronaService.doFetchSnapshot(todayDate))
        }

        get("/counter") {
            call.respond(counterService.getCounter().counter)
        }

        get("/feedbacks") {
            call.respond(feedbackService.getFeedback().toList())
        }

        post("/feedbacks") {
            val feedback = call.receive<Feedback>()
            call.respond(feedbackService.addFeedback(feedback).toList())
        }
    }
}
