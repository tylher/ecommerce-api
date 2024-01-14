package dev.damola.ecommerce.controller;

import dev.damola.ecommerce.Dto.CreateUserDto;
import dev.damola.ecommerce.Dto.LoginDto;
import dev.damola.ecommerce.model.User;
import dev.damola.ecommerce.service.UserService;
import dev.damola.ecommerce.system.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;
    @PostMapping("/signup")
    ResponseEntity<?> userSignUp(@Valid @RequestBody CreateUserDto createUserDto){
        User user = userService.createUser(createUserDto);
        return new ResponseEntity<Result>(new Result(true,"user created successfully",user)
                , HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<?> userLogin(@Valid @RequestBody LoginDto loginDto){
        HashMap<String, Object> response = userService.login(loginDto);
        return new ResponseEntity<Result>(new Result(true,"user created successfully",response)
                , HttpStatus.OK);
    }
}
