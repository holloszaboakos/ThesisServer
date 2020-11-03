package web

import com.codahale.metrics.Slf4jReporter
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.config.HoconApplicationConfig
import io.ktor.features.*
import io.ktor.gson.GsonConverter
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.metrics.dropwizard.*
import io.ktor.routing.*
import web.apis.LifecicleApi
import web.apis.SetupApi
import web.apis.UpdateApi
import java.time.Duration
import java.util.concurrent.TimeUnit


internal val settings = HoconApplicationConfig(ConfigFactory.defaultApplication(HTTP::class.java.classLoader))

object HTTP {
    val client = HttpClient(Apache)
}

fun Application.main() {
    install(DefaultHeaders)
    install(DropwizardMetrics) {
        val reporter = Slf4jReporter.forRegistry(registry)
                .outputTo(log)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build()
        reporter.start(10, TimeUnit.SECONDS)
    }
    install(ContentNegotiation) {
        register(ContentType.Application.Json, GsonConverter())
    }
    install(AutoHeadResponse) // see http://ktor.io/features/autoheadresponse.html
    install(HSTS, ApplicationHstsConfiguration()) // see http://ktor.io/features/hsts.html
    install(Compression, ApplicationCompressionConfiguration()) // see http://ktor.io/features/compression.html
    install(Locations) // see http://ktor.io/features/locations.html
    install(Routing) {
        LifecicleApi()
        SetupApi()
        UpdateApi()
    }
    install(CORS){
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
        maxAge = Duration.ofDays(1)
        allowNonSimpleContentTypes = true
    }

    environment.monitor.subscribe(ApplicationStopping)
    {
        HTTP.client.close()
    }
}