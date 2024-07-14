package runner

import zio.http.*
import zio.*
import _root_.http.server.endpoint.*
import zio.http.endpoint.openapi.*
import zio.http.codec.PathCodec


object Main extends ZIOAppDefault {

  import PathCodec.*

  private val logLevel: Status => LogLevel = (incomingStatus: Status) =>
    if (incomingStatus.isSuccess) LogLevel.Info
    else LogLevel.Error

  override def run: ZIO[Any, Nothing, Unit] = (for {
    (healthEndpoints, healthRoutes) <- ZIO.serviceWith[HealthCheckEndpointsAlg]{ service => (service.endpoints, service.routes) }
    loggingMiddleware = Middleware.requestLogging(
      logRequestBody = true,
      logResponseBody = true,
      level = logLevel
    )
    composedEndpoints = healthEndpoints // add new endpoints here
    openAPI = OpenAPIGen.fromEndpoints(title = "ZIO Http Swagger Example", version = "1.0", composedEndpoints)
    swaggerRoute = SwaggerUI.routes("docs" / "openapi", openAPI)
    composedRoutesWithLogging = (healthRoutes ++ swaggerRoute) @@ loggingMiddleware // add new routes here
    _ <- Server.serve(composedRoutesWithLogging)
  } yield ()).provide(
    HealthCheckEndpoints.live,
    Server.default.orDie
  )
}
