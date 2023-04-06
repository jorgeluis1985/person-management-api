package io.github.nataelienai.usermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
  info = @Info(
    title = "User Manager API",
    version = "0.0.1",
    description = "API for creating, retrieving and updating people and their addresses."
  )
)
@SpringBootApplication
public class UserManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserManagerApplication.class, args);
  }

}
