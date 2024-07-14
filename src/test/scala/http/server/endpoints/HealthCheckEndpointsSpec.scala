package http.server.endpoints

import zio.*
import zio.http.*
import zio.test.*

object HealthCheckEndpointsSpec extends ZIOSpecDefault {

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("HealthCheckEndpoints")(
      test("returns 200") {
        for {
          routes <- ZIO.serviceWith[_root_.http.server.endpoint.HealthCheckEndpointsAlg](_.routes)
          url <- ZIO.fromEither(URL.decode("/status"))
          validStatusRequest = Request(
            method = Method.GET,
            url = url
          )
          response <- routes.runZIO(validStatusRequest)
          body <- response.body.asString
        } yield assertTrue(
          response.status == Status.Ok,
          body.contains("Hello World!")
        )
      }
    ).provide(_root_.http.server.endpoint.HealthCheckEndpoints.live)

}
