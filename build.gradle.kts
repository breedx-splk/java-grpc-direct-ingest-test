plugins {
    application
}

repositories {
    mavenCentral()
}

val otelVersion = "1.30.1"

application {
    mainClass.set("com.splunk.example.Application")
    applicationDefaultJvmArgs = listOf(
        "-Dotel.service.name=grcp.test"
    )
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api:${otelVersion}")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:${otelVersion}")
    implementation("io.opentelemetry:opentelemetry-sdk-extension-autoconfigure:${otelVersion}")
}
