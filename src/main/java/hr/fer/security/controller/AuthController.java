package hr.fer.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import hr.fer.entity.auth.User;
import hr.fer.security.CurrentUser;
import hr.fer.security.CustomUserDetailsService;
import hr.fer.security.JwtAuthenticationResponse;
import hr.fer.security.JwtTokenProvider;
import hr.fer.security.UserPrincipal;
import hr.fer.security.requests_responses.ApiResponse;
import hr.fer.security.requests_responses.LoginRequest;
import hr.fer.security.requests_responses.SignUpRequest;
import hr.fer.security.requests_responses.editUserRequest;

import java.net.URI;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService service;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        User user = service.getUserById(tokenProvider.getUserIdFromJWT(jwt));

        return ResponseEntity.ok(JwtAuthenticationResponse.builder().accessToken(jwt).id(user.getId()).name(user.getName()).email(user.getEmail()).build());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (service.userExistsByUserName(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (service.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = User
                .builder()
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = service.createUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PutMapping("/editUser")
    public ResponseEntity<?> editUser(@RequestBody editUserRequest editUserRequest, @CurrentUser UserPrincipal loggedInUser) {

        if (loggedInUser == null)
            return new ResponseEntity(new ApiResponse(false, "You are not logged in to do that!"),
                    HttpStatus.BAD_REQUEST);

        if (service.userExistsByUserName(editUserRequest.getNewUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (service.existsByEmail(editUserRequest.getNewEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = service.getUserById(loggedInUser.getId());

        if (editUserRequest.getNewName() != null)
            user.setName(editUserRequest.getNewName());
        if (editUserRequest.getNewUsername() != null)
            user.setUsername(editUserRequest.getNewUsername());
        if (editUserRequest.getNewEmail() != null)
            user.setEmail(editUserRequest.getNewEmail());
        if (editUserRequest.getNewPassword() != null)
            user.setPassword(passwordEncoder.encode(editUserRequest.getNewPassword()));

        service.editUser(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String jwt = tokenProvider.generateToken(authentication);

        user = service.getUserById(tokenProvider.getUserIdFromJWT(jwt));

        return ResponseEntity.ok(JwtAuthenticationResponse.builder().accessToken(jwt).id(user.getId()).name(user.getName()).email(user.getEmail()).build());
    }

}
