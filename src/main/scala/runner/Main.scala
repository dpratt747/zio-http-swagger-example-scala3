package runner

import zio.http.*
import zio.*
import _root_.http.server.endpoint.*

object Main extends ZIOAppDefault {

  override def run: ZIO[Any, Nothing, Unit] = (for {
    routes <- ZIO.serviceWith[HealthCheckEndpointsAlg](_.routes)
    _ <- Server.serve(routes)
  } yield ()).provide(
    HealthCheckEndpoints.live,
    Server.default.orDie
  )
}
