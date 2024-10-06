package com.lostfly.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.server.webjars.*
import io.ktor.sse.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }
    install(SwaggerUI) {
        swagger {
            swaggerUrl = "swagger-ui"
            forwardRoot = true
        }
        info {
            title = "Example API"
            version = "latest"
            description = "Example API for testing and demonstration purposes."
        }
        server {
            url = "http://localhost:8080"
            description = "Development Server"
        }
    }
    install(Resources)
    install(SSE)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/webjars") {
            call.respondText("<script src='/webjars/jquery/jquery.js'></script>", ContentType.Text.Html)
        }
        get<Articles> { article ->
            // Get all articles ...
            call.respond("List of articles sorted starting from ${article.sort}")
        }
        sse("/hello") {
            send(ServerSentEvent("world"))
        }
    }
}

@Serializable
@Resource("/articles")
class Articles(val sort: String? = "new")
