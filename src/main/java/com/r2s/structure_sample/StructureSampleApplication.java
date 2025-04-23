package com.r2s.structure_sample;

import com.r2s.structure_sample.common.enums.Role;
import com.r2s.structure_sample.dto.AuthRequest;
import com.r2s.structure_sample.entity.User;
import com.r2s.structure_sample.repository.UserRepository;
import com.r2s.structure_sample.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class StructureSampleApplication {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(StructureSampleApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner() {
//		return args -> {
//			User admin = User.builder().email("admin@gmail.com").password(passwordEncoder.encode("admin@123")).role(Role.ADMIN).firstName("Admin").lastName("Admin").build();
//			User manager = User.builder().email("manager@gmail.com").password(passwordEncoder.encode("manager@123")).role(Role.MANAGER).firstName("Manager").lastName("Manager").build();
//			userRepository.saveAll(List.of(admin, manager));
//		};
//	}

}
