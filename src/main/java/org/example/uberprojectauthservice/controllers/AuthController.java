package org.example.uberprojectauthservice.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.uberprojectauthservice.dtos.AuthRequestDto;
import org.example.uberprojectauthservice.dtos.AuthResponseDto;
import org.example.uberprojectauthservice.dtos.PassengerDto;
import org.example.uberprojectauthservice.dtos.PassengerSignupRequestDto;
import org.example.uberprojectauthservice.services.AuthService;
import org.example.uberprojectauthservice.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${cookie.expiry}")
    private int cookieExpiry;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response = authService.signupPassenger(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDto authRequestDto, HttpServletResponse response) {
        System.out.println("Request received " + authRequestDto.getEmail() + " " + authRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));
        if(authentication.isAuthenticated()) {
            String jwtToken = jwtService.createToken(authRequestDto.getEmail());

            ResponseCookie cookie = ResponseCookie.from("JwtToken", jwtToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(7*24*3600)
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new ResponseEntity<>(AuthResponseDto.builder().success(true).build(), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Inside validate controller");

        for(Cookie cookie : request.getCookies()) {
            System.out.println(cookie.getName() + " " + cookie.getValue());
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
