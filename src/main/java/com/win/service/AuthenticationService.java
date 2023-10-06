package com.win.service;

import com.win.models.ApplicationUser;
import com.win.models.Role;
import com.win.repository.RoleRepository;
import com.win.repository.UserRepository;
import com.win.utils.ExecutionTimeLogger;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @ExecutionTimeLogger
    public ApplicationUser registerUser(String username, String password){
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        return userRepository.save(ApplicationUser.
                builder().userId(0)
                .username(username)
                .password(passwordEncoder.encode(password))
                .authorities(authorities).build());
    }
}
