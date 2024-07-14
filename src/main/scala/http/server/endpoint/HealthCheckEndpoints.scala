package http.server.endpoint

import _root_.http.server.domain.StatusResponse
import zio.http.*
import zio.http.codec.HttpContentCodec
import zio.http.endpoint.Endpoint
import zio.http.endpoint.EndpointMiddleware.None
import zio.*


trait HealthCheckEndpointsAlg {
  def endpoints: List[Endpoint[Unit, Unit, ZNothing, ? >: StatusResponse & String <: Serializable, None]]
  def routes: Routes[Any, Response]
}

final case class HealthCheckEndpoints() extends HealthCheckEndpointsAlg {


  /***
   * first set of endpoints to confirm composing more than one endpoint works
   */
  private val helloEndpoint =
    Endpoint(Method.GET / Root / "hello").out[String]

  private val helloRoute = helloEndpoint.implement { _ =>
    ZIO.succeed("Hello stranger")
  }

  /***
   * second set of endpoints
   */

  private val getStatusEndpoint =
    Endpoint(Method.GET / Root / "status").out[StatusResponse]

  private val getStatusRoute = getStatusEndpoint.implement { _ =>
    ZIO.succeed(StatusResponse("Hello World!"))
  }
  def endpoints: List[Endpoint[Unit, Unit, ZNothing, ? >: StatusResponse & String <: Serializable, None]] = List(
    getStatusEndpoint,
    helloEndpoint
  )

  def routes: Routes[Any, Response] = Routes.fromIterable(List(
    getStatusRoute,
    helloRoute
  ))

}

object HealthCheckEndpoints {
  val live: ZLayer[Any, Nothing, HealthCheckEndpointsAlg] = ZLayer.fromFunction(() => HealthCheckEndpoints.apply())
}
