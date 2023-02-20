package com.qs.security.controller;

import com.qs.security.domain.request.LoginRequest;
import com.qs.security.domain.response.LoginResponse;
import com.qs.security.domain.response.UserResponse;
import com.qs.security.entity.User;
import com.qs.security.exception.InvalidCredentialsException;
import com.qs.security.security.AuthUserDetail;
import com.qs.security.security.JwtProvider;
import com.qs.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private AuthenticationManager authenticationManager;
    private UserService userService;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager,
                                         UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    private JwtProvider jwtProvider;

    @Autowired
    public void setJwtProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    //User trying to log in with username and password
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws InvalidCredentialsException{
//        System.out.println("in login");
        System.out.println(request.getUsername() + " " + request.getPassword());
        Authentication authentication;

        //Try to authenticate the user using the username and password
        try{
          authentication = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
          );
        } catch (Exception e){
            e.printStackTrace();
            throw new InvalidCredentialsException("Incorrect credentials, please try again.");
        }

        //Successfully authenticated user will be stored in the authUserDetail object
        AuthUserDetail authUserDetail = (AuthUserDetail) authentication.getPrincipal(); //getPrincipal() returns the user object

        //A token wil be created using the username/email/userId and permission
        String token = jwtProvider.createToken(authUserDetail);

//        System.out.println("return password key");
        //Returns the token as a response to the frontend/postman
        return LoginResponse.builder()
                .message("Welcome " + authUserDetail.getUsername())
                .token(token)
                .build();

    }

    @PutMapping("/register")
    public UserResponse registerUser(
            @RequestBody User user) throws InvalidCredentialsException {

        System.out.println(user);
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null)
            throw new InvalidCredentialsException("Null input not allowed");

        User exist_user = userService.getUserByName(user.getUsername());

        if (exist_user != null)
            throw new InvalidCredentialsException("User with name: " + user.getUsername() + " already exists!");

        exist_user = userService.getUserByEmail(user.getEmail());

        if (exist_user != null)
            throw new InvalidCredentialsException("User with email: " + user.getEmail() + " already exists!");

        Integer user_id = userService.createNewUser(user);
        return UserResponse.builder()
                .message("User registers successfully! Your user_id is: " + user_id)
                .user(user)
                .build();
    }
}
