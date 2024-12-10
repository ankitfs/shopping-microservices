package com.ankit.api.gateway.service;

import com.ankit.api.gateway.dto.SignupRequest;
import com.ankit.api.gateway.entity.UserEntity;
import com.ankit.api.gateway.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signup(SignupRequest signupRequest){
        String email = signupRequest.getEmail();

        Optional<UserEntity> userEntity = userRepository.findByEmail(signupRequest.getEmail());

        if (userEntity.isPresent()) {
            throw new DuplicateKeyException("Email Already Exists");
        }

        String hashPassword = passwordEncoder.encode(signupRequest.getPassword());

        UserEntity userEntityNew = UserEntity.builder()
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .password(hashPassword)
                .createdDate(LocalDate.now())
                .build();

        userEntityNew  = userRepository.save(userEntityNew);

        log.info("New User has been saved : {} ", userEntityNew.getId());
    }
}
