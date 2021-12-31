package com.jingzhe.corona

import com.jingzhe.corona.model.Feedback
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

import kotlin.test.*

class ApplicationTest {
    @Test
    fun testSnapshot() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/snapshot").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testOldSnapshot() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/old-snapshot?date=2021-12-01").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                println(response.content)
            }
        }
    }

    @Test
    fun testFetchSnapshot() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/fetch-snapshot").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                println(response.content)
            }
        }
    }
    @Test
    fun testCounters() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/counters").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                println(response.content)
            }
        }
    }

    @Test
    fun testGetFeedback() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/feedbacks").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                println(response.content)
            }
        }
    }

    @Test
    fun testPostFeedback() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, "/feedbacks") {
                addHeader("content-type", "application/json")
                setBody(Json.encodeToString(Feedback(name = "from unit test", subject = "test_sub", content = "test_content")))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

}