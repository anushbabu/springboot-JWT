package com.spring.security.spring_jwt_security.service;

import com.spring.security.spring_jwt_security.entity.MyUser;
import com.spring.security.spring_jwt_security.entity.UserDetailsBean;
import com.spring.security.spring_jwt_security.repo.MyRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MyRepo myRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<MyUser> userNameByName = myRepo.findByUserName(userName);
        return userNameByName.map(UserDetailsBean::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));
    }

    public String saveNewUser(MyUser myUser) {
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myRepo.save(myUser);
        return myUser.getUserName() + " saved successfully";
    }
}
