package com.win;

import com.win.models.ApplicationUser;
import com.win.models.Role;
import com.win.repository.RoleRepository;
import com.win.repository.UserRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class AuthenticatedBackendApplication {

    @Bean
    CommandLineRunner runner(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {

            if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
            Role adminRole = roleRepository.save(Role.builder().authority("ADMIN").build());
            roleRepository.save(Role.builder().authority("USER").build());

            Set<Role> roleSet = new HashSet<>();
            roleSet.add(adminRole);

            ApplicationUser admin = new ApplicationUser(1, "admin", passwordEncoder.encode("password"), roleSet);
            userRepository.save(admin);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthenticatedBackendApplication.class, args);
    }

}
