package com.spring.security.spring_jwt_security.controller;

import com.spring.security.spring_jwt_security.entity.MyUser;
import com.spring.security.spring_jwt_security.service.MyService;
import com.spring.security.spring_jwt_security.service.MyUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class MyController {

    private final MyService myService;
    private final MyUserDetailsService myUserDetailsService;

    @PostMapping("/addUser")
    public String addUser(@RequestBody MyUser user){
        return myService.saveID(user);
    }

    @GetMapping("/getAllUsers")
    public Iterable<MyUser> getAllUsers() {
        return myService.getAllUsers();
    }

    @GetMapping("/getAllUsers/{id}")
    public MyUser getById(@PathVariable("id") int id) {
        return myService.getUserNameById(id).get();
    }

    @PostMapping("/newuser")
    public String newuser(@RequestBody MyUser user){

        return myUserDetailsService.saveNewUser(user);
    }

}
