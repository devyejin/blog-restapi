package me.yejin.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.yejin.springbootdeveloper.domain.User;
import me.yejin.springbootdeveloper.dto.AddUserRequest;
import me.yejin.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(bcryptPasswordEncoder.encode(dto.getPassword()))
                .build())
                .getId();
    }
}
