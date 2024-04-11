package com.jobhunt.inftrastructure.userloginandregister.controller;

public record JwtResponseDto(
        String username,
        String token) {
}
