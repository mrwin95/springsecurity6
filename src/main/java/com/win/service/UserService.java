package com.win.service;

import com.win.models.ApplicationUser;
import com.win.repository.UserRepository;
import com.win.utils.ExecutionTimeLogger;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final CacheService cacheService;
    @Override
    @ExecutionTimeLogger
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetails> cachedToken = (Optional<UserDetails>) cacheService.getCache(username);
        if(cachedToken != null){
            logger.info("call cache", cachedToken);
            return cachedToken.get();
        }
//        System.out.println("In the user details service");

        UserDetails users = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(users != null){
            logger.info("call set cache");
            cacheService.setCache(username, users);
        }
        return users;
    }

    public Page<ApplicationUser> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }
}
