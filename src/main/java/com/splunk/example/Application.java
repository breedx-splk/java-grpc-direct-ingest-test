package com.splunk.example;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) throws Exception {
        new Application().run();
    }

    private void run() throws Exception {
        Properties props = readProperties();
        OpenTelemetry sdk = initializeOtel(props);
        Tracer tracer = sdk.getTracer("splunk.example.testing");
        int attempt = 0;
        while(true){
            attempt = attempt + 1;
            SpanBuilder spanBuilder = tracer.spanBuilder("example.test");
            spanBuilder.setAttribute("attempt.num", attempt);
            Span span = spanBuilder.startSpan();
            try (Scope scope = span.makeCurrent()) {
                System.out.print(new Date());
                System.out.print(" - Doing span work...");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("done.");
            }
            finally {
                span.end();
            }
        }
    }

    private OpenTelemetry initializeOtel(Properties props) {
        System.out.println("Initializing otel sdk...");

        System.setProperty("otel.exporter.otlp.protocol", "http/protobuf");
        System.setProperty("otel.resource.attributes", "service.name=grpc.test.app,environment=grpc.test");

        String realm = props.getProperty("realm");
        String endpoint = "https://ingest." + realm + ".signalfx.com/v2/trace/otlp";
        System.setProperty("otel.exporter.otlp.traces.endpoint", endpoint);

        String token = props.getProperty("token");
        System.setProperty("otel.exporter.otlp.traces.headers", "X-SF-TOKEN=" + token);
        return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    }

    private static Properties readProperties() throws IOException {
        Properties props = new Properties();
        System.out.println("Loading properties...");
        props.load(new FileReader(new File("env.props")));
        String realm = props.getProperty("realm");
        if(realm == null){
            throw new IllegalStateException("Missing realm in env.props");
        }
        String token = props.getProperty("token");
        if(token == null){
            throw new IllegalStateException("Missing token in env.props");
        }
        return props;
    }

}