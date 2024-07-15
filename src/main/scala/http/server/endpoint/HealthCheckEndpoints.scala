package http.server.endpoint

import http.server.domain.StatusResponse
import zio.*
import zio.http.*
import zio.http.codec.HttpContentCodec
import zio.http.endpoint.Endpoint
import zio.http.endpoint.EndpointMiddleware.None


trait HealthCheckEndpointsAlg {
  def endpoints: List[Endpoint[Unit, Unit, ZNothing, ? >: StatusResponse & String <: Serializable, None]]
  def routes: Routes[Any, Response]
}

final case class HealthCheckEndpoints() extends HealthCheckEndpointsAlg {


  /***
   * First set of endpoints and routes to confirm composing more than one endpoint works
   */
  private val helloEndpoint =
    Endpoint(Method.GET / Root / "hello").out[String]

  private val helloRoute = helloEndpoint.implement { _ =>
    ZIO.succeed("Hello stranger")
  }

  /***
   * Second set of endpoints and routes
   */

  private val getStatusEndpoint =
    Endpoint(Method.GET / Root / "status").out[StatusResponse]

  private val getStatusRoute = getStatusEndpoint.implement { _ =>
    ZIO.succeed(StatusResponse("Hello World!"))
  }
  
  /***
   * Returns the public endpoints and routes
   */
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
