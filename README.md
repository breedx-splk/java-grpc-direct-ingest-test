
# gRPC Direct Ingest Sample App

This app will attempt to do send very simple span data directly to 
a Splunk ingest datapoint. The data will be sent using `grpc` with data 
encoded as protobuf.

To run this app, you must first create a file named `env.props` that contains
the realm (typically us0, us1, eu0, etc)
to which you want to send data, and the secret ingest token.

```
realm=us0
token=abc123-your-token-here
```

and then run the application:

```
./gradlew run
```

A span will be created every 1 second. After a few minutes the data should be available 
in the Splunk O11y Suite UI. To narrow down finding the data, you may wish to enter
the environment (`grpc.test`) or the service name (`grpc.test.app`).
