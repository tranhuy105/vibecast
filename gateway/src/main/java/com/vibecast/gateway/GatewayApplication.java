package com.vibecast.gateway;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Supplier;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator vibecastRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		Supplier<String> currentTimeSupplier = () -> LocalDateTime.now().toString();

		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/vibecast/views/**")
						.filters(f -> applyFilters(f, currentTimeSupplier, "viewsCircuitBreaker"))
						.uri("lb://VIEWS"))
				.route(p -> p
						.path("/vibecast/streams/**")
						.filters(f -> applyFilters(f, currentTimeSupplier, "streamsCircuitBreaker"))
						.uri("lb://STREAMS"))
				.route(p -> p
						.path("/vibecast/analyzes/**")
						.filters(f -> applyFilters(f, currentTimeSupplier, "analyzesCircuitBreaker", false, true))
						.uri("lb://ANALYZES"))
				.build();
	}

	private GatewayFilterSpec applyFilters(GatewayFilterSpec filters, Supplier<String> currentTimeSupplier, String circuitBreakerName) {
		return applyFilters(filters, currentTimeSupplier, circuitBreakerName, true, true);
	}

	private GatewayFilterSpec applyFilters(GatewayFilterSpec filters, Supplier<String> currentTimeSupplier, String circuitBreakerName, boolean useCircuitBreaker, boolean useRateLimiter) {
		filters = filters
				.rewritePath("^/vibecast/.*?/(?<segment>.*)", "/${segment}")
				.addResponseHeader("X-Response-Time", currentTimeSupplier.get());

		if (useCircuitBreaker) {
			filters = filters.circuitBreaker(config -> config.setName(circuitBreakerName)
					// .setFallbackUri("forward:/fallback")
			);
		}

		if (useRateLimiter) {
			filters = filters.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter()).setKeyResolver(userKeyResolver()));
		}

		return filters;
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(60,120,1);
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}
}
