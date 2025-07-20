package com.free.library;

import com.free.library.service.ApiCall;
import com.free.library.service.ConsoleMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(ConsoleMenu consoleMenu){
		return args -> {
			consoleMenu.displayMenu();
		};
	}



}
