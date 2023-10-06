package com.win.service;

import com.win.dto.LoginResponseDto;
import com.win.models.ApplicationUser;
import com.win.models.Role;
import com.win.repository.RoleRepository;
import com.win.repository.UserRepository;
import com.win.utils.ExecutionTimeLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final AuthenticationManager authenticationManager;
    private final  TokenService tokenService;

    @ExecutionTimeLogger
    public ApplicationUser registerUser(String username, String password){
        Role userRole = roleRepository.findByAuthority("USER").get();
        String encodedPassword = passwordEncoder.encode(password);
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        ApplicationUser applicationUser = ApplicationUser.
                builder().userId(0)
                .username(username)
                .password(encodedPassword)
                .authorities(authorities).build();

        return userRepository.save(applicationUser);
    }

    public LoginResponseDto loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            String token = tokenService.generateJwt(auth);
            return new LoginResponseDto(userRepository.findByUsername(username).get(), token, "");
        }catch (AuthenticationException e){
            return new LoginResponseDto(null, "", "");
        }
    }
}
