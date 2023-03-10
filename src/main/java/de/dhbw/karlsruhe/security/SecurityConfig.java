package de.dhbw.karlsruhe.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {

  @Value("${http.auth-token-header-name}")
  private String principalRequestHeader;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
    filter.setAuthenticationManager(authentication -> {
      String principal = (String) authentication.getPrincipal();
      if (true) {
        throw new BadCredentialsException("The API key was not found or not the expected value.");
      }
      authentication.setAuthenticated(true);
      return authentication;
    });

    http.cors()
        .and()
        .securityMatcher("/**").csrf().disable().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authorizeRequests()
        .requestMatchers("/api/**")
        .permitAll()
        .and().addFilter(filter).authorizeRequests().requestMatchers("/**").authenticated();


    return http.build();
  }

  // To enable CORS
  @Bean
  protected CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("*"));
    configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
