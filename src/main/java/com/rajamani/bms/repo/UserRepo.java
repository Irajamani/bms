package com.rajamani.bms.repo;

import com.rajamani.bms.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepo extends ElasticsearchRepository<User, String> {

    User findByUsername(String username);
}
