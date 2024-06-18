package com.spring.auth_test.repositories;

import com.spring.auth_test.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsersRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

}
