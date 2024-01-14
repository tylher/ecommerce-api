package dev.damola.ecommerce.service;

import dev.damola.ecommerce.Dto.CreateUserDto;
import dev.damola.ecommerce.Dto.LoginDto;
import dev.damola.ecommerce.configuration.Jwt.JwtUtils;
import dev.damola.ecommerce.configuration.UserPrincipal;
import dev.damola.ecommerce.converter.UserDtoToUserConverter;
import dev.damola.ecommerce.exceptions.CustomException;
import dev.damola.ecommerce.exceptions.NotfoundException;
import dev.damola.ecommerce.model.User;
import dev.damola.ecommerce.repository.UserRepository;
import dev.damola.ecommerce.system.Result;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
        private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
        @Autowired
        UserRepository userRepository;

        @Autowired
        UserDtoToUserConverter userDtoToUserConverter;
        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        AuthenticationManager authenticationManager;
        @Autowired
        JwtUtils jwtUtils;
        public User createUser(@Valid CreateUserDto createUserDto){
            try{
             if(userRepository.existsByEmail(createUserDto.email())){
                 throw new CustomException("user with this email exists already");
             }
             User user= userDtoToUserConverter.convert(createUserDto);
            assert user != null;
            user.setPassword(passwordEncoder.encode(user.getPassword()));
             return userRepository.save(user);
            }
            catch (CustomException ex){
                LOGGER.error(ex.getMessage());
                throw new CustomException(ex.getMessage());
            }
            catch (Exception ex){
                throw new CustomException("Unable to create your account");
            }
        }

        public HashMap<String, Object> login(LoginDto loginDto){
            try {
                User savedUser = userRepository.findByEmail(loginDto.email()).orElseThrow(
                        () -> new NotfoundException("user with this email not found")
                );
                UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());
                Authentication authentication = authenticationManager.authenticate(loginToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String token = jwtUtils.issue(userPrincipal.getUserId(), userPrincipal.getUserId(),
                        userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("token", token);
                map.put("user", savedUser);
                return map;
            }catch (BadCredentialsException ex){
                throw new CustomException("Email or password is incorrect");
            }
            catch (Exception e){
                LOGGER.error(e.getMessage());
                throw new CustomException("User login not successful");
            }
        }
}
