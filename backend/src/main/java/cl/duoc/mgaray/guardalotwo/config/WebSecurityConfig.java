package cl.duoc.mgaray.guardalotwo.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    var permitAll = new String[] {"/products"};
    var corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("*"));
    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
    corsConfiguration.setAllowedHeaders(List.of("*"));

    http.cors(
        httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(request -> corsConfiguration));
    http.authorizeHttpRequests(
            requests ->
                requests.requestMatchers(permitAll).permitAll().anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }
}
