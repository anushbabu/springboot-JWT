package com.spring.security.spring_jwt_security.service;

import com.spring.security.spring_jwt_security.entity.MyUser;
import com.spring.security.spring_jwt_security.repo.MyRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MyService {

    private final MyRepo myRepo;

    private final PasswordEncoder passwordEncoder;

    public Optional<MyUser> getUserNameById(int id) {
        return myRepo.findById(id);
    }

    public Optional<MyUser> getUserNameByName(String userName) {
        return myRepo.findByUserName(userName);
    }

    public String saveID(MyUser myUser) {
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myRepo.save(myUser);
        return myUser.getUserName() + " saved successfully";
    }

    public List<MyUser> getAllUsers() {
        return myRepo.findAll();
    }

    public String updateUserName(int id, String newUserName) {
        return myRepo.findById(id).map(user -> {
            user.setUserName(newUserName);
            myRepo.save(user);
            return "User name updated successfully";
        }).orElse("User not found");
    }

    public String deleteUserById(int id) {
        return myRepo.findById(id).map(user -> {
            myRepo.delete(user);
            return "User deleted successfully";
        }).orElse("User not found");
    }

}
