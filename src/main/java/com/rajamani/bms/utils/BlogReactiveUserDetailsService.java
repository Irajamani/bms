package com.rajamani.bms.utils;

import com.rajamani.bms.domain.User;
import com.rajamani.bms.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Service
public class BlogReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepo userRepo;

    public BlogReactiveUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public Mono<UserDetails> findByUsername(String s) {
        User user = userRepo.findByUsername(s);
        if (user==null){
            return Mono.empty();
        }
        return Mono.just(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Arrays.asList(new
                SimpleGrantedAuthority(user.getRole()))));
    }
}
