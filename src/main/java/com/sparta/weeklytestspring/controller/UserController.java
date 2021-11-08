package com.sparta.weeklytestspring.controller;

import com.sparta.weeklytestspring.domain.User;
import com.sparta.weeklytestspring.dto.ResponseDto;
import com.sparta.weeklytestspring.dto.SignupRequestDto;
import com.sparta.weeklytestspring.dto.TokenResponseDto;
import com.sparta.weeklytestspring.security.UserDetailsServiceImpl;
import com.sparta.weeklytestspring.service.UserService;
import com.sparta.weeklytestspring.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupRequestDto requestDto) {
        log.info("login username = {}", requestDto.getUsername());
        User savedUser = userService.signup(requestDto);

        return ResponseDto.builder()
                .result("success")
                .message("회원가입 성공")
                .httpStatus(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public TokenResponseDto signin(@RequestBody SignupRequestDto requestDto) {
        log.info("로그인 요청 = {}", requestDto.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("로그인에 실패했습니다.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(requestDto.getUsername());
        String token = jwtUtils.createToken(userDetails.getUsername());
        log.info("로그인 후 토큰 = {}", token);
        return new TokenResponseDto(token, "로그인에 성공했습니다.");
    }
}
