package com.elijah.onlinebankingapp;

import com.elijah.onlinebankingapp.security.model.Role;
import com.elijah.onlinebankingapp.security.model.UserModel;
import com.elijah.onlinebankingapp.security.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class OnlineBankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineBankingAppApplication.class, args);


	}
	/*@Bean
	CommandLineRunner runner(UserService userService){
		return args -> {
			userService.saveRole(new Role(null,"SUPER_ADMIN"));
			userService.saveRole(new Role(null,"ADMIN"));
			userService.saveRole(new Role(null,"USER"));
			userService.saveRole(new Role(null,"MANAGER"));
			userService.saveUser(new UserModel(null,"Ukeme Elijah","Ukemsco","ukeme",new ArrayList<>()));
			userService.saveUser(new UserModel(null,"Emediong Dan","Ekababa","emediong",new ArrayList<>()));

			userService.addRoleToUser("Ukemsco","SUPER_ADMIN");
			userService.addRoleToUser("Ukemsco","ADMIN");
			userService.addRoleToUser("Ekababa","USER");
		};
	}
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}*/

}
