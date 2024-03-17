package com.jobhunt.domain.registerandlogin;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryLoginAndRegisterRepository implements RegisterRepository {

     Map<String,User> database = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(database.get(username));
    }

    @Override
    public User save(User user) {
        UUID userId = UUID.randomUUID();
        User userForDb = new User(
                userId.toString(),
                user.username(),
                user.password()
        );
        database.put(userForDb.username(),userForDb);
        return userForDb;
    }
}
