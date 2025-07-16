package com.example.Personality.Services;

import com.example.Personality.Exception.DuplicatedEntity;
import com.example.Personality.Exception.NotFound;
import com.example.Personality.Models.Role;
import com.example.Personality.Models.User;
import com.example.Personality.Repositories.UserRepository;
import com.example.Personality.Requests.LoginRequest;
import com.example.Personality.Requests.RegisterRequest;
import com.example.Personality.Requests.UpdateRequest;
import com.example.Personality.Responses.AccountResponse;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.*;
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

    public User getUserById(long id) {
        return userRepository.findUserByIdAndIsDeletedFalse(id);
    }

    public void register(RegisterRequest registerRequest) {
        User user = modelMapper.map(registerRequest, User.class);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicatedEntity("Email already exists!");
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new DuplicatedEntity("Phone number already exists!");
        }
        try {
            String originPassword = user.getPassword();
            user.setPassword(passwordEncoder.encode(originPassword));
            User newAccount = userRepository.save(user);
            modelMapper.map(newAccount, AccountResponse.class);
        } catch (DataIntegrityViolationException e) {
            // Nếu DB có unique constraint thì lỗi sẽ vào đây
            if (e.getMessage().toLowerCase().contains("email")) {
                throw new DuplicatedEntity("Duplicate email!");
            } else if (e.getMessage().toLowerCase().contains("phone")) {
                throw new DuplicatedEntity("Duplicate phone!");
            } else {
                throw new DuplicatedEntity("Duplicated field!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unknown error during registration.");
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

            User account = (User) authentication.getPrincipal();
            AccountResponse response = modelMapper.map(account, AccountResponse.class);
            response.setToken(tokenService.generateToken(account));

            return response;

        } catch (BadCredentialsException e) {
            // Sai mật khẩu hoặc email không đúng
            throw new RuntimeException("Email hoặc mật khẩu không chính xác");
        } catch (DisabledException e) {
            // Tài khoản bị vô hiệu hóa
            throw new RuntimeException("Tài khoản đã bị vô hiệu hóa");
        } catch (LockedException e) {
            // Tài khoản bị khóa
            throw new RuntimeException("Tài khoản đã bị khóa");
        } catch (AccountExpiredException e) {
            throw new RuntimeException("Tài khoản đã hết hạn");
        } catch (CredentialsExpiredException e) {
            throw new RuntimeException("Mật khẩu đã hết hạn");
        } catch (Exception e) {
            // Lỗi hệ thống khác
            throw new RuntimeException("Đăng nhập thất bại: " + e.getMessage());
        }
    }


    public void resetPassword(String newPass) {
        User account = getCurrentAccount();
        account.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(account);
    }

    public User updateAccount(UpdateRequest registerRequest, long id) {
        User account = userRepository.findUserByIdAndIsDeletedFalse(id);
        if (account == null) {
            throw new NotFound("Account not exist!");
        }
        // Kiểm tra email,số điện thoại đã tồn tại chưa (ngoại trừ tài khoản hiện tại)
        if (!account.getEmail().equals(registerRequest.getEmail()) && userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicatedEntity("Email already exists!");
        }
        if (!account.getPhone().equals(registerRequest.getPhone()) && userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new DuplicatedEntity("Phone number already exists!");
        }
        account.setEmail(registerRequest.getEmail());
        account.setFullname(registerRequest.getUsername());
        account.setPhone(registerRequest.getPhone());
        return userRepository.save(account);
    }

    public User getCurrentAccount() {
        User account = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByIdAndIsDeletedFalse(account.getId());
    }

    public void linkStudentToParent(Long parentId, String studentEmail) {
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new NotFound("Không tìm thấy phụ huynh với ID: " + parentId));

        User student = userRepository.findByEmail(studentEmail);
        if (student == null) new NotFound("Không tìm thấy học sinh với email: " + studentEmail);

        student.setParentId(String.valueOf(parent.getId()));
        userRepository.save(student);
    }

    public List<User> getChildrenOfParent(Long parentId) {
        return userRepository.findByParentId(String.valueOf(parentId));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailAndIsDeletedFalse(email);
    }

    public User getUserByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
