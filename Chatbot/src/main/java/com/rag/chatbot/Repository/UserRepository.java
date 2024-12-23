package com.rag.chatbot.Repository;

import com.rag.chatbot.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Check if a user exists with the given email
    boolean existsByEmail(String email);

    // Find a user by email
    Optional<User> findByEmail(String email);


}
