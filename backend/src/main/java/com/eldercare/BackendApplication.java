package com.eldercare;

import com.eldercare.modules.admin.user_management.UserEntity;
import com.eldercare.modules.admin.user_management.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner activateAdmin(UserRepository userRepository) {
		return args -> {
			Optional<UserEntity> userOpt = userRepository.findByEmailAndIsDeletedFalse("daniel.brooks@nhms-demo.local");
			if (userOpt.isPresent()) {
				UserEntity user = userOpt.get();
				if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
					user.setStatus("ACTIVE");
					userRepository.save(user);
					System.out.println(">>> [STARTUP] Activated daniel.brooks@nhms-demo.local");
				}
			}
		};
	}
}
