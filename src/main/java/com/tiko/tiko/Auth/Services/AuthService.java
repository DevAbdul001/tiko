package com.tiko.tiko.Auth.Services;

import com.tiko.tiko.Auth.DTO.RegisterDTO;
import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Long registerUser(RegisterDTO dto){
        boolean exists = userRepo.existsByEmail(dto.email());

        if(exists){
            throw new RuntimeException("Email already exists");
        }
        String passwordHash = passwordEncoder.encode(dto.password());
        User user = new User(
                dto.name(),
                dto.email(),
                passwordHash
        );
        userRepo.save(user);

        return user.getId();
    }

}
