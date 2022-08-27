package com.kunis.linkuni.service;

import com.kunis.linkuni.entity.User;
import com.kunis.linkuni.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(final User userEntity) {
        if(userEntity == null || userEntity.getName() == null ) {
            throw new RuntimeException("Invalid arguments");
        }
        final String name = userEntity.getName();
        if(userRepository.existsByName(name)) {
            log.warn("name already exists {}", name);
            throw new RuntimeException("name already exists");
        }

        return userRepository.save(userEntity);
    }

    public User getByCredentials(final String name, final String password, final PasswordEncoder encoder) {
        final User originalUser = userRepository.findByName(name);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }
}