package com.roadsidehelp.api.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // MAIN OPENAPI CONFIG
    @Bean
    public OpenAPI roadsideHelpAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RoadsideHelp API Documentation 1")
                        .description("Complete backend API reference for the Roadside Assistance Platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RoadsideHelp Team")
                                .email("support@roadsidehelp.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes(
                                "BearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }

    // GROUPED APIS
    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("Authentication")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userGroup() {
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/api/v1/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi mechanicGroup() {
        return GroupedOpenApi.builder()
                .group("Mechanic")
                .pathsToMatch("/api/v1/mechanics/**")
                .build();
    }

    @Bean
    public GroupedOpenApi sosGroup() {
        return GroupedOpenApi.builder()
                .group("SOS")
                .pathsToMatch("/api/v1/sos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminGroup() {
        return GroupedOpenApi.builder()
                .group("Admin Panel")
                .pathsToMatch("/api/v1/admin/**")
                .build();
    }
}
