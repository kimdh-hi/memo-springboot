package com.sparta.weeklytestspring.service;

import com.sparta.weeklytestspring.domain.User;
import com.sparta.weeklytestspring.dto.SignupRequestDto;
import com.sparta.weeklytestspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), encodedPassword);
        User savedUser = userRepository.save(user);

        return savedUser;
    }
}
