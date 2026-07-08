package com.tiko.tiko.Users.Service;

import com.tiko.tiko.Users.Entity.User;
import com.tiko.tiko.Users.Repository.UserRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User getUserById(Long id){
        return userRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }
}
