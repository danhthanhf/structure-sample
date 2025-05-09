package com.r2s.structure_sample;

import com.r2s.structure_sample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
