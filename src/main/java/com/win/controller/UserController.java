package com.win.controller;

import com.win.models.ApplicationUser;
import com.win.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public String helloUserController(){
        return "user access level";
    }

//    @GetMapping("/users")
//    public Page<ApplicationUser> getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
//                                          @RequestParam(name = "size", defaultValue = "25") int size){
//        Pageable pageable = PageRequest.of(page, size);
//        return userService.getAllUsers(pageable);
//
//    }
}
