package com.shopee.ecommer.shopeebegatewaydemo;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class ShopeeBeGatewayDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopeeBeGatewayDemoApplication.class, args);
	}


	@Bean
	public RouteLocator routeConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/account/**")
						.filters( f -> f.rewritePath("/account/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())

								.circuitBreaker(config -> config.setName("accountCircuitBreaker")
										.setFallbackUri("forward:/contactSupport")))

						.uri("lb://ACCOUNT"))
				.route(p -> p
						.path("/admin/**")
						.filters( f -> f.rewritePath("/admin/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
//								.retry(retryConfig -> retryConfig.setRetries(3)
//										.setMethods(HttpMethod.GET)
//										.setBackoff(Duration.ofMillis(1000),Duration.ofMillis(1000),2,true)))
						.uri("lb://ADMIN"))
				.route(p -> p
						.path("/email/**")
						.filters( f -> f.rewritePath("/email/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
//								.retry(retryConfig -> retryConfig.setRetries(3)
//										.setMethods(HttpMethod.GET)
//										.setBackoff(Duration.ofMillis(1000), Duration.ofMillis(1000),2,true))

						)

						.uri("lb://EMAIL"))
			.build();
	}

	// set limit time for circuirbreaker
	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(20)).build()).build());
	}


}
