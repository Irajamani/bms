package com.rajamani.bms.Service;


import com.rajamani.bms.domain.User;
import com.rajamani.bms.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public User save(User user){
        return userRepo.save(user);
    }

    public User getUserByName(String name){
        return userRepo.findByUsername(name);
    }

    public void delete(){
        userRepo.deleteAll();
    }
}
