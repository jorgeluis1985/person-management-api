package io.github.nataelienai.personmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
  info = @Info(
    title = "Person Management API",
    version = "0.0.1",
    description = "API for creating, retrieving and updating people and their addresses."
  )
)
@SpringBootApplication
public class PersonManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(PersonManagementApplication.class, args);
  }

}
