package com.example.mini_saas.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration for observability (metrics, tracing).
 *
 * Spring Boot Actuator automatically configures:
 * - Micrometer metrics
 * - Prometheus endpoint at /actuator/prometheus
 * - Health checks at /actuator/health
 */
@Configuration
public class ObservabilityConfig {

}