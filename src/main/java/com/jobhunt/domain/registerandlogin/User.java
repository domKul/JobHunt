package com.jobhunt.domain.registerandlogin;

import lombok.Builder;

@Builder
record User(String id,
        String username,
        String password) {

}
