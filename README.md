# What this contains:
* zio-http endpoint examples
* reading from file and sending files via http

### Swagger/Open api endpoints for the following:

#### Sending files
```bash
curl -X 'POST' \
  'http://localhost:8080/file' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/octet-stream' \
  --data-binary '@README.md'
```
#### Getting files:
```bash
curl -X 'GET' \
  'http://localhost:8080/file' \
  -H 'accept: application/octet-stream'
```
#### Status endpoint:
```bash
curl -X 'GET' \
  'http://localhost:8080/status' \
  -H 'accept: application/json'
```

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
