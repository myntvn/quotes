package com.mynt.quickstart

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

private val quotes = listOf(
    Quote(1, "To be or not to be, that is the question.", "William Shakespeare", "From Hamlet, Act 3, Scene 1. This is one of the most famous soliloquies in literature."),
    Quote(2, "I think, therefore I am.", "René Descartes", "Originally 'Cogito, ergo sum' in Latin. A fundamental element of Western philosophy."),
    Quote(3, "The only thing we have to fear is fear itself.", "Franklin D. Roosevelt", "From his first inaugural address in 1933, during the Great Depression."),
    Quote(4, "That's one small step for man, one giant leap for mankind.", "Neil Armstrong", "Spoken when he became the first person to step onto the lunar surface in 1969.")
)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        get("/quotes") {
            call.respond(quotes)
        }
        get("/quotes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val quote = quotes.find { it.id == id }
            if (quote != null) {
                call.respond(quote)
            } else {
                call.respondText("Quote not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
    }
}