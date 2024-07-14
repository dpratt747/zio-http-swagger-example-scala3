# To run the application:

```bash
sbt "runMain runner.Main"
```

and

```bash
curl --location 'http://localhost:8080/status'
````
or
```bash
curl --location 'http://localhost:8080/hello'
```

## To reach the open api/swagger docs use this url: 
`http://localhost:8080/docs/openapi`

Relevant documentation:
* https://zio.dev/zio-http/examples/endpoint/
* https://zio.dev/zio-schema/
* https://zio.dev/zio-json/