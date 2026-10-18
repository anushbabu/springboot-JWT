package com.spring.security.spring_jwt_security.repo;

import com.spring.security.spring_jwt_security.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MyRepo extends JpaRepository<MyUser, Integer> {
    Optional<MyUser> findByUserName(String userName);

    @Override
    Optional<MyUser> findById(Integer integer);
}
