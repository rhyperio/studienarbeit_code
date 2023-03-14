package de.dhbw.karlsruhe.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SpringdocConfig {

  @Bean
  public OpenAPI baseOpenAPI() {
    return new OpenAPI().info(new Info()
        .title("Studienarbeit: Typ-2-Grammatik & Kellerautomaten")
        .version("0.0.1")
        .description("Generate and validate exercises for type-2-grammars and push-down-automaton"));
  }

}
