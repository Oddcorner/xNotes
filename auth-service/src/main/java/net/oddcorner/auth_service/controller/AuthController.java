package net.oddcorner.auth_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.oddcorner.auth_service.dto.RegisterRequest;
import net.oddcorner.auth_service.dto.UserSummaryResponse;
import net.oddcorner.auth_service.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserSummaryResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        UserSummaryResponse userSummaryResponse = userService.registerUser(registerRequest);
        return new ResponseEntity<>(userSummaryResponse, HttpStatus.CREATED);
    }

}
