// File: TracingApp.java
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;

import java.time.Duration;

public class TracingApp {
    public static void main(String[] args) throws InterruptedException {
        // Setup OTLP Exporter
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint("http://otel-collector.default.svc.cluster.local:4317") // Update if needed
                .setTimeout(Duration.ofSeconds(5))
                .build();

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .build();

        OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .buildAndRegisterGlobal();

        Tracer tracer = GlobalOpenTelemetry.getTracer("demo-app");

        // Simulate sending spans
        while (true) {
            Span span = tracer.spanBuilder("demo-span").startSpan();
            try {
                Thread.sleep(1000); // Do some work
            } finally {
                span.end();
            }
        }
    }
}

