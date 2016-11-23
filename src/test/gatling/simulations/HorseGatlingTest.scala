import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the Horse entity.
 */
class HorseGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://127.0.0.1:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "X-XSRF-TOKEN" -> "${xsrf_token}"
    )

    val scn = scenario("Test the Horse entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))
        .check(headerRegex("Set-Cookie", "XSRF-TOKEN=(.*);[\\s]").saveAs("xsrf_token"))).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authentication")
        .headers(headers_http_authenticated)
        .formParam("j_username", "admin")
        .formParam("j_password", "admin")
        .formParam("remember-me", "true")
        .formParam("submit", "Login")
        .check(headerRegex("Set-Cookie", "XSRF-TOKEN=(.*);[\\s]").saveAs("xsrf_token"))).exitHereIfFailed
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all horses")
            .get("/api/horses")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new horse")
            .post("/api/horses")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "horseName":"SAMPLE_TEXT", "horseStatus":"SAMPLE_TEXT", "birthDate":"2020-01-01T00:00:00.000Z", "owner":"SAMPLE_TEXT", "stewardsEmbargoes":"SAMPLE_TEXT", "emergencyVaccinationRecordURL":"SAMPLE_TEXT", "lastGearChange":"2020-01-01T00:00:00.000Z", "trainer":"SAMPLE_TEXT", "prize":null, "bonus":null, "mimMaxDistWin":"SAMPLE_TEXT", "firstUpData":"SAMPLE_TEXT", "secondUpData":"SAMPLE_TEXT", "horseTrack":"SAMPLE_TEXT", "horseDist":"SAMPLE_TEXT", "horseClass":"SAMPLE_TEXT", "positionInLastRace":"0", "firm":"SAMPLE_TEXT", "good":"SAMPLE_TEXT", "soft":"SAMPLE_TEXT", "heavy":"SAMPLE_TEXT"}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_horse_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created horse")
                .get("${new_horse_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created horse")
            .delete("${new_horse_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
