package com.jobhunt.inftrastructure.userloginandregister.controller;

import lombok.Builder;

@Builder
public record JwtResponseDto(
        String username,
        String token) {
}
