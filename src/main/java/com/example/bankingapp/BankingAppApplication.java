package com.example.bankingapp;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "The Java banking project",
                description = "Project java using spring boot with CRUD basic",
                version = "v1.0",
                contact = @Contact(
                        name = "Phan Quoc Khanh",
                        email = "khanhphanquoc68@gmail.com",
                        url = "google.com"
                ),
                license = @License(
                        name = "Khanh Academy",
                        url = "google.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "The Java Tutorial",
                url = "google.com"
        )
)
public class BankingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingAppApplication.class, args);
    }

}
