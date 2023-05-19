/*
package com.elijah.onlinebankingapp.security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.elijah.onlinebankingapp.exception.DataAlreadyExistException;
import com.elijah.onlinebankingapp.exception.DataNotFoundException;
import com.elijah.onlinebankingapp.response.customer.SignUpResponse;
import com.elijah.onlinebankingapp.security.dto.RoleToUserDto;
import com.elijah.onlinebankingapp.security.model.Role;
import com.elijah.onlinebankingapp.security.model.UserModel;
import com.elijah.onlinebankingapp.security.service.UserService;
import com.elijah.onlinebankingapp.security.service.UserServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
    @PostMapping("/api/users/save")
    public ResponseEntity<UserModel> saveUser(@RequestBody UserModel userModel) throws DataAlreadyExistException {
        return new ResponseEntity<>(userService.saveUser(userModel),HttpStatus.CREATED);
    }
    @PostMapping("/api/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) throws DataAlreadyExistException {
        return new ResponseEntity<>(userService.saveRole(role),HttpStatus.CREATED);
    }
    @PostMapping("/api/addRoleToUser")
    public ResponseEntity<SignUpResponse> addRoleToUser(@RequestBody RoleToUserDto roleToUserDto) throws DataNotFoundException, DataAlreadyExistException {
        userService.addRoleToUser(roleToUserDto.getUsername(),roleToUserDto.getRoleName());
        return new ResponseEntity<>(new SignUpResponse(true,"Role Added to user successfully"),HttpStatus.OK);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws DataNotFoundException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                UserModel appUser = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+60 *60 *1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",appUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception exception){
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }else {
            throw   new DataNotFoundException("Refresh Token is Missing");
        }
    }

}
*/