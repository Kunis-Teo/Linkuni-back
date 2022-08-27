package com.kunis.linkuni.controller;

import com.kunis.linkuni.dto.ResponseDTO;
import com.kunis.linkuni.dto.user.UserDTO;
import com.kunis.linkuni.entity.User;
import com.kunis.linkuni.security.TokenProvider;
import com.kunis.linkuni.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 리퀘스트를 이용해 저장할 유저 만들기
            User user = User.builder()
                    .name(userDTO.getName())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();
            // 서비스를 이용해 리파지토리에 유저 저장
            User registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .name(registeredUser.getName())
                    .id(registeredUser.getId())
                    .build();
            // 유저 정보는 항상 하나이므로 그냥 리스트로 만들어야하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴.
            return ResponseEntity.ok(responseUserDTO);
        } catch (Exception e) {
            // 예외가 나는 경우 bad 리스폰스 리턴.
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<UserDTO> authenticate(@RequestBody UserDTO userDTO) {
        User user = userService.getByCredentials(
                userDTO.getName(),
                userDTO.getPassword(),
                passwordEncoder);

//        if(user != null) {
            // 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .name(user.getName())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
//        }
//        else {
//            ResponseDTO responseDTO = ResponseDTO.builder()
//                    .error("Login failed.")
//                    .build();
//            return ResponseEntity
//                    .badRequest()
//                    .body(responseDTO);
//        }
    }
}