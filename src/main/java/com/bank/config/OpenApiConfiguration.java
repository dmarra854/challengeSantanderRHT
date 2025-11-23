package com.bank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {

        String description = """
                Hexagonal Architecture Microservice for Bank Account Management

                ## Features
                - Hexagonal Architecture
                - SOLID Principles
                - Comprehensive Error Handling
                - Complete CRUD Operations

                ## Error Codes
                - **4001**: Entity not found
                - **4002**: Entity already exists
                - **4101**: Invalid Entity number
                - **4102**: Invalid Entity holder
                - **4103**: Invalid Entity type
                - **4105**: Invalid currency
                """;

        return new OpenAPI()
                .info(new Info()
                        .title("Bank Microservice API")
                        .version("1.0.0")
                        .description(description)
                        .contact(new Contact()
                                .name("Bank Team")
                                .email("support@bank.example.com"))
                        .license(new License()
                                .name("Apache 2.0")))
                .addServersItem(new Server().url("http://localhost:9091").description("Local Environment"))
                .addTagsItem(new Tag().name("Accounts").description("Operations related to bank accounts"))
                .addTagsItem(new Tag().name("Health").description("System health checks"));
    }
}
