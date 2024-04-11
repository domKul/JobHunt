package com.jobhunt.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface RegisterRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
