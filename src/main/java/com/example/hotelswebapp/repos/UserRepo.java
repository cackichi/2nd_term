package com.example.hotelswebapp.repos;

import com.example.hotelswebapp.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
    @NonNull
    List<UserEntity> findAll();
    Optional<UserEntity> findById(int id);
}