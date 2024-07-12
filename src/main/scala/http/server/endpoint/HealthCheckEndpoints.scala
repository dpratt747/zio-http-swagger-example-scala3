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

  val jsonSuccessResponse =
    """
      |{
      | "output": "Hello World!"
      |}
      |""".stripMargin

  private val getStatusEndpoint =
    Endpoint(Method.GET / Root / "status").out[String]

  private val getStatusRoute = getStatusEndpoint.implement{ _ =>
    ZIO.logInfo("get status route").as(jsonSuccessResponse)
  }


  private val openAPI = OpenAPIGen.fromEndpoints(title = "Swagger Example", version = "1.0", getStatusEndpoint)

  def routes: Routes[Any, Response] = Routes.fromIterable(List(
    getStatusRoute
  )) ++ SwaggerUI.routes("docs" / "openapi", openAPI)

}

object HealthCheckEndpoints {
  val live: ZLayer[Any, Nothing, HealthCheckEndpointsAlg] = ZLayer.fromFunction(() => HealthCheckEndpoints.apply())
}
