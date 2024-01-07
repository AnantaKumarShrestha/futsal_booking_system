package com.intern.futsalBookingSystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Futsal Booking System",description = "API documentation of Futsal Booking System"))
public class FutsalBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FutsalBookingSystemApplication.class, args);
	}


}
