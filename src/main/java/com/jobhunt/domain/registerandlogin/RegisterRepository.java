package com.jobhunt.domain.registerandlogin;

import java.util.Optional;

 interface RegisterRepository {
    Optional<User> findByUsername(String username);

    User save(User user);
}
