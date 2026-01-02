package com.mynt.quickstart

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

object Quotes : Table() {
    val id = integer("id").autoIncrement()
    val text = varchar("text", 1024)
    val author = varchar("author", 255)
    val description = varchar("description", 1024)

    override val primaryKey = PrimaryKey(id)
}



fun initDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/quotes_db", // Update with your DB details
        driver = "org.postgresql.Driver",
        user = "postgres", // Update with your DB user
        password = "postgres" // Update with your DB password
    )

    transaction {
        create(Quotes)
        if (Quotes.selectAll().count() == 0L) {
            Quotes.insert {
                it[text] = "To be or not to be, that is the question."
                it[author] = "William Shakespeare"
                it[description] = "From Hamlet, Act 3, Scene 1."
            }
            Quotes.insert {
                it[text] = "I think, therefore I am."
                it[author] = "René Descartes"
                it[description] = "Originally 'Cogito, ergo sum' in Latin."
            }
        }
    }
}

fun Application.module() {
    initDatabase()
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        get("/quotes") {
            val quotes = transaction {
                Quotes.selectAll().map {
                    Quote(it[Quotes.id], it[Quotes.text], it[Quotes.author], it[Quotes.description])
                }
            }
            call.respond(quotes)
        }
        get("/quotes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val quote = transaction {
                Quotes.select { Quotes.id eq id }.singleOrNull()?.let {
                    Quote(it[Quotes.id], it[Quotes.text], it[Quotes.author], it[Quotes.description])
                }
            }

            if (quote != null) {
                call.respond(quote)
            } else {
                call.respond(HttpStatusCode.NotFound, "Quote not found")
            }
        }
    }
}