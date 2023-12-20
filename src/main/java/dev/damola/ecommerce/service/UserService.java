package dev.damola.ecommerce.service;

import dev.damola.ecommerce.Dto.CreateUserDto;
import dev.damola.ecommerce.exceptions.CustomException;
import dev.damola.ecommerce.exceptions.NotfoundException;
import dev.damola.ecommerce.model.User;
import dev.damola.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
        @Autowired
        UserRepository userRepository;
        @Autowired
        PasswordEncoder passwordEncoder;
        public User createUser(CreateUserDto createUserDto){
             userRepository.findByEmail(createUserDto.email().toLowerCase()).orElseThrow(()->
                     new CustomException("user with this email exists already")
             );
             User user= new User();
             user.setEmail(createUserDto.email().toLowerCase());
             user.setPassword(passwordEncoder.encode(createUserDto.password()));
             user.setRoles(createUserDto.roles());

             return userRepository.save(user);

        }
}
