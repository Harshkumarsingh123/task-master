package com.taskmanager.service;

import com.taskmanager.dto.UserRequest;
import com.taskmanager.dto.UserResponse;
import com.taskmanager.entity.User;
import com.taskmanager.exception.EmailAlreadyExistsException;
import com.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    UserService(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder){

        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public UserResponse createUser(UserRequest userRequest){

        if(userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email Already Exists");
        }

        User user=new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());

        User saveUser=userRepository.save(user);

        return new UserResponse(
                saveUser.getId(),
                saveUser.getName(),
                saveUser.getEmail(),
                saveUser.getRole()
        );
    }
}
