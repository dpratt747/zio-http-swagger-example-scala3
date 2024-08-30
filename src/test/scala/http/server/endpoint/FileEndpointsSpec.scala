package http.server.endpoint

import http.server.domain.StatusResponse
import zio.*
import zio.http.*
import zio.json.*
import zio.test.*

object FileEndpointsSpec extends ZIOSpecDefault {

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("FileEndpoints")(
      getFileTests,
      postFileTests
    ).provide(FilesEndpoints.live)

  private val getFileTests = suite("GET /file")(
    test("returns 200 and a proper body when GET /file is called") {
      for {
        routes <- ZIO.serviceWith[FilesEndpointsAlg](_.routes)
        url <- ZIO.fromEither(URL.decode("/file"))
        request = Request(
          method = Method.GET,
          url = url
        )
        response <- routes.runZIO(request)
        body <- response.body.asString
      } yield assertTrue(
        response.status == Status.Ok,
        body == "some string"
      )
    }
  )

  private val postFileTests = suite("/Hello")(
    test("returns 200 when POST /file is called") {
      for {
        routes <- ZIO.serviceWith[FilesEndpointsAlg](_.routes)
        url <- ZIO.fromEither(URL.decode("/file"))
        message = "Some input message"
        validStatusRequest = Request(
          method = Method.POST,
          url = url,
          body = Body.fromString(message)
        )
        response <- routes.runZIO(validStatusRequest)
        body <- response.body.asString
        expected = StatusResponse(message)
      } yield assertTrue(
        response.status == Status.Ok,
        body == expected.toJson
      )
    }
  )

}
