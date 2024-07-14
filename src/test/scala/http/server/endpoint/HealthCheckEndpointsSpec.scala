package http.server.endpoint

import zio.*
import zio.http.*
import zio.test.*
import zio.json.*
import _root_.http.server.domain.StatusResponse

object HealthCheckEndpointsSpec extends ZIOSpecDefault {

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("HealthCheckEndpoints")(
      test("returns 200 and a proper json body") {
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
    ).provide(HealthCheckEndpoints.live)

}
