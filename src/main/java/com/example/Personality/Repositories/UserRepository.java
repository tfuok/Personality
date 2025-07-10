package com.example.Personality.Repositories;

import com.example.Personality.Models.Role;
import com.example.Personality.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUserByIsDeletedFalseOrderByRole();

    User findUserByIdAndIsDeletedFalse(long id);

    User findByEmailAndIsDeletedFalse(String email);

    User findByRole(Role role);
}
