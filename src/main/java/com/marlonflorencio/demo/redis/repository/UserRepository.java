package com.marlonflorencio.demo.redis.repository;

import com.marlonflorencio.demo.redis.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {}