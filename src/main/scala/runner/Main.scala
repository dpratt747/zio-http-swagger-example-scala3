package runner

import zio.http.*
import zio.*
import _root_.http.server.endpoint.*

object Main extends ZIOAppDefault {

  private val testRoute = Routes(Method.ANY / Root -> handler(Response.notFound("")))

  override def run: ZIO[Any, Nothing, Unit] = (for {
    _ <- ZIO.logInfo(s"Reached run method not yet retrieved the routes")
    routes <- ZIO.serviceWith[HealthCheckEndpointsAlg](_.routes)
//    _ <- ZIO.logInfo(s"Reached run method retrieved routes: ${routes}")
//    _ <- Server.serve(testRoute) // todo this works?
    _ <- Server.serve(routes)
  } yield ()).provide(
    HealthCheckEndpoints.live,
    Server.default.orDie
  )
}
