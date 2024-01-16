package dev.damola.ecommerce.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import dev.damola.ecommerce.Dto.CreateUserDto;
import dev.damola.ecommerce.Dto.LoginDto;
import dev.damola.ecommerce.configuration.Jwt.JwtUtils;
import dev.damola.ecommerce.configuration.UserPrincipal;
import dev.damola.ecommerce.converter.UserDtoToUserConverter;
import dev.damola.ecommerce.enums.Role;
import dev.damola.ecommerce.exceptions.CustomException;
import dev.damola.ecommerce.exceptions.NotfoundException;
import dev.damola.ecommerce.model.User;
import dev.damola.ecommerce.repository.UserRepository;
import dev.damola.ecommerce.system.Result;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

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

        @Value("clientId")
        private String clientId;
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
                List<String> userRoles = userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList();
               return getUserAndToken(savedUser,userPrincipal.getUserId(),userPrincipal.getEmail(),userRoles);
            }catch (BadCredentialsException ex){
                throw new CustomException("Email or password is incorrect");
            }
            catch (Exception e){
                LOGGER.error(e.getMessage());
                throw new CustomException("User login not successful");
            }
        }

        public HashMap<String, Object> signInWithGoogle(String credential) throws GeneralSecurityException, IOException {
            try {
                HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

                GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(transport,
                        GsonFactory.getDefaultInstance()
                ).setAudience(Collections.singleton(clientId)).build();
                GoogleIdToken token = googleIdTokenVerifier.verify(credential);
                if (token != null) {
                    Payload payload = token.getPayload();
                    String email = payload.getEmail();
                    String googleId = payload.getSubject();

                    Optional<User> supposedUser = userRepository.findByEmail(email);

                    if (supposedUser.isPresent()) {
                        List<String> userRoles = Arrays.stream(supposedUser.get().getRoles().split("")).toList();
                        return getUserAndToken(supposedUser.get(), supposedUser.get().getUserId()
                                , supposedUser.get().getEmail(), userRoles);
                    }

                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setRoles(Role.USER.getTypeValue());
                    newUser.setUserId(googleId);

                    User savedUser = userRepository.save(newUser);
                    List<String> userRoles = Arrays.stream(savedUser.getRoles().split("")).toList();
                    return getUserAndToken(savedUser, savedUser.getUserId(), savedUser.getEmail(), userRoles);
                } else {
                    throw new NotfoundException("Your credential is invalid");
                }
            }
            catch (Exception ex){
                throw new CustomException(ex.getMessage());
            }
        }

        private HashMap<String,Object> getUserAndToken(User user,String id,String email,List<String> roles){
            String jwtToken = jwtUtils.issue(id, email,roles);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("token", jwtToken);
            map.put("user", user);
            return map;
        }
}
