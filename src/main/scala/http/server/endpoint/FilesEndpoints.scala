package http.server.endpoint

import http.server.domain.StatusResponse
import zio.*
import zio.http.*
import zio.http.Header.ContentDisposition
import zio.http.Header.ContentDisposition.Attachment
import zio.http.codec.{HttpCodec, HttpContentCodec}
import zio.http.endpoint.Endpoint
import zio.http.endpoint.EndpointMiddleware.None
import zio.stream.ZStream


trait FilesEndpointsAlg {
  def endpoints: List[Endpoint[Unit, ? >: ZStream[Any, Nothing, Byte] & Unit, ZNothing, ? >: StatusResponse & ZStream[Any, Nothing, Byte] <: Object, None]]

  def routes: Routes[Any, Response]
}

final case class FilesEndpoints() extends FilesEndpointsAlg {

  /**
   * First set of endpoints and routes POST (file)
   */

  private val postFileEndpoint =
    Endpoint(Method.POST / Root / "file")
      .inStream[Byte]
      .out[StatusResponse]

  private val postFileRoute = postFileEndpoint.implement { inputStream =>
    for {
      collectedStream <- inputStream.runCollect
      res = new String(collectedStream.toArray)
    } yield StatusResponse(res)
  }

  /**
   * Second set of endpoints and routes GET (file)
   */

  private val getFileEndpoint =
    Endpoint(Method.GET / Root / "file")
      .outCodec(
        HttpCodec.contentStream[Byte] ++
          HttpCodec.contentDisposition.const(
            ContentDisposition.Attachment(Some("filename.txt"))
          ) /*++ HttpCodec.status(Status.Ok)*/ // todo: edit this to change the status code response
      )

  private val getFileRoute = getFileEndpoint.implement { _ =>
    for {
      str <- ZIO.succeed("some string")
      stream = ZStream.fromIterable(str.getBytes())
    } yield stream
  }


  /**
   * Returns the public endpoints and routes
   */
  def endpoints: List[Endpoint[Unit, ? >: ZStream[Any, Nothing, Byte] & Unit, ZNothing, ? >: StatusResponse & ZStream[Any, Nothing, Byte] <: Object, None]] = List(
    postFileEndpoint,
    getFileEndpoint
  )

  def routes: Routes[Any, Response] = Routes.fromIterable(List(
    postFileRoute,
    getFileRoute
  ))

}

object FilesEndpoints {
  val live: ZLayer[Any, Nothing, FilesEndpointsAlg] = ZLayer.fromFunction(() => FilesEndpoints.apply())
}
