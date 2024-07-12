package http.server.endpoint

import zio.*
import zio.http.*
import zio.http.codec.PathCodec
import zio.http.endpoint.Endpoint
import zio.http.endpoint.openapi.*

trait HealthCheckEndpointsAlg {
  def routes: Routes[Any, Response]
}

final case class HealthCheckEndpoints() extends HealthCheckEndpointsAlg {

  import PathCodec.*

  // todo: replace with zio json
  val jsonSuccessResponse =
    """
      |{
      | "output": "Hello World!"
      |}
      |""".stripMargin

  val notFoundEndpointResponse =
    """
      |{
      | "output": "This endpoint has not been implemented!"
      |}
      |""".stripMargin

//  private val defaultNotFoundEndpoint = Endpoint(Method.ANY / Root).out[String]

  private val getStatusEndpoint =
    Endpoint(Method.GET / Root / "status").out[String]

  private val getStatusRoute = getStatusEndpoint.implement{ _ =>
    ZIO.logInfo("get status route").as(jsonSuccessResponse)
  }

//  private val defaultNotFoundRoute: Route[Any, Nothing] = defaultNotFoundEndpoint.implement(_ => ZIO.succeed(notFoundEndpointResponse))

  private val openAPI = OpenAPIGen.fromEndpoints(title = "Swagger Example", version = "1.0", getStatusEndpoint/*, defaultNotFoundEndpoint*/)

  def routes: Routes[Any, Response] = Routes(getStatusRoute/*, defaultNotFoundRoute*/) ++ SwaggerUI.routes("docs" / "openapi", openAPI)

//  val routes: Routes[Any, Nothing] =
//    Routes(
//      Method.GET / Root / "status" -> handler(Response.json(jsonSuccessResponse)),
//      Method.ANY / Root -> handler(Response.notFound("This endpoint does not exist"))
//    ) ++ SwaggerUI.routes("docs" / "openapi", openAPI) SwaggerUI.routes("docs" / "openapi", openAPI)

}

object HealthCheckEndpoints {
  val live: ZLayer[Any, Nothing, HealthCheckEndpointsAlg] = ZLayer.fromFunction(() => HealthCheckEndpoints.apply())
}
