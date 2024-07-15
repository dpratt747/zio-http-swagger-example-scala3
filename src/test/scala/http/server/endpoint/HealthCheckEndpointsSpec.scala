package http.server.endpoint

import http.server.domain.StatusResponse
import zio.*
import zio.http.*
import zio.json.*
import zio.test.*

object HealthCheckEndpointsSpec extends ZIOSpecDefault {

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("HealthCheckEndpoints")(
      statusEndpointTests,
      helloEndPointTests
    ).provide(HealthCheckEndpoints.live)

  private val statusEndpointTests = suite("/Status")(
    test("returns 200 and a proper json body when GET /status is called") {
      for {
        routes <- ZIO.serviceWith[HealthCheckEndpointsAlg](_.routes)
        url <- ZIO.fromEither(URL.decode("/status"))
        validStatusRequest = Request(
          method = Method.GET,
          url = url
        )
        response <- routes.runZIO(validStatusRequest)
        body <- response.body.asString
        expected = StatusResponse("Hello World!")
      } yield assertTrue(
        response.status == Status.Ok,
        body == expected.toJson
      )
    }
  )

  private val helloEndPointTests = suite("/Hello")(
    test("returns 200 when GET /hello is called") {
      for {
        routes <- ZIO.serviceWith[HealthCheckEndpointsAlg](_.routes)
        url <- ZIO.fromEither(URL.decode("/hello"))
        validStatusRequest = Request(
          method = Method.GET,
          url = url
        )
        response <- routes.runZIO(validStatusRequest)
        body <- response.body.asString
      } yield assertTrue(
        response.status == Status.Ok,
        body == "Hello stranger".toJson
      )
    }
  )

}
