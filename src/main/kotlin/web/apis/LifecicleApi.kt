/**
* DRP
* This is the main server of QLM's DRP system
*
* OpenAPI spec version: 1.0.0
* Contact: akos.hollo-szabo@qlndc.hu
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package io.swagger.server.apis

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authentication
import io.ktor.auth.basicAuthentication
import io.ktor.auth.oauth
import io.ktor.auth.OAuthAccessTokenResponse
import io.ktor.auth.OAuthServerSettings
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*

import kotlinx.coroutines.experimental.asCoroutineDispatcher

// ktor 0.9.x is missing io.ktor.locations.DELETE, this adds it.
// see https://github.com/ktorio/ktor/issues/288


fun Route.LifecicleApi() {
    val gson = Gson()
    val empty = mutableMapOf<String, Any?>()

    route("/lifecycle/clean") {
        post {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
    

    route("/lifecycle/pause") {
        post {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
    

    route("/lifecycle/prepare") {
        post {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
    

    route("/lifecycle/resume") {
        post {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
    

    route("/lifecycle/start") {
        post {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
    

    route("/lifecycle/stop") {
        post {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
    
}
