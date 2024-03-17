package com.jobhunt.domain.user;

import lombok.Builder;

@Builder
record User(String id,
        String username,
        String password) {

}
