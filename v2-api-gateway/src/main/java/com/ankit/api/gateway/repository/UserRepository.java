package com.ankit.api.gateway.repository;

import com.ankit.api.gateway.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    public Optional<UserEntity> findByEmail(String email);
}
