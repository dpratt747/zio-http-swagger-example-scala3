package http.server.endpoint

import zio.*
import zio.http.*
import zio.http.endpoint.Endpoint
import zio.http.endpoint.EndpointMiddleware.None

trait HealthCheckEndpointsAlg {
  def endpoints: List[Endpoint[Unit, Unit, ZNothing, String, None]]
  def routes: Routes[Any, Response]
}

final case class HealthCheckEndpoints() extends HealthCheckEndpointsAlg {
  
  private val jsonSuccessResponse =
    """
      |{
      | "output": "Hello World!"
      |}
      |""".stripMargin

  private val getStatusEndpoint =
    Endpoint(Method.GET / Root / "status").out[String]

  private val getStatusRoute = getStatusEndpoint.implement { _ =>
    ZIO.succeed(jsonSuccessResponse)
  }
  def endpoints: List[Endpoint[Unit, Unit, ZNothing, String, None]] = List(
    getStatusEndpoint
  )
  
  def routes: Routes[Any, Response] = Routes.fromIterable(List(
    getStatusRoute
  ))

}

object HealthCheckEndpoints {
  val live: ZLayer[Any, Nothing, HealthCheckEndpointsAlg] = ZLayer.fromFunction(() => HealthCheckEndpoints.apply())
}
