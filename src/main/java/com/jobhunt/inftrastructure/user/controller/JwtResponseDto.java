package com.jobhunt.inftrastructure.user.controller;

public record JwtResponseDto(
        String username,
        String token) {
}
