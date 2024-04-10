package com.jobhunt.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

 interface RegisterRepository extends MongoRepository<User,String> {
    Optional<User> findByUsername(String username);
 }
