package com.example.Personality.Services;

import com.example.Personality.Exception.DuplicatedEntity;
import com.example.Personality.Models.Role;
import com.example.Personality.Models.User;
import com.example.Personality.Repositories.UserRepository;
import com.example.Personality.Requests.LoginRequest;
import com.example.Personality.Requests.RegisterRequest;
import com.example.Personality.Responses.AccountResponse;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenService tokenService;
    public List<User> getAllAccount() {
        return userRepository.findUserByIsDeletedFalseOrderByRole();
    }

    public AccountResponse register(RegisterRequest registerRequest) {
        User user = modelMapper.map(registerRequest, User.class);
        try {
            String originPassword = user.getPassword();
            user.setPassword(passwordEncoder.encode(originPassword));
            User newAccount = userRepository.save(user);
            return modelMapper.map(newAccount, AccountResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains(user.getEmail())) {
                throw new DuplicatedEntity("Duplicate email!");
            } else {
                throw new DuplicatedEntity("Duplicate phone");
            }
        }
    }

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Extract the authenticated account
            User account = (User) authentication.getPrincipal();
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            accountResponse.setToken(tokenService.generateToken(account));

            return accountResponse;

        } catch (BadCredentialsException e) {
            throw new EntityNotFoundException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public User getCurrentAccount() {
        User account = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByIdAndIsDeletedFalse(account.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailAndIsDeletedFalse(email);
    }

    public User getUserByRole(Role role){
        return userRepository.findByRole(role);
    }
}
