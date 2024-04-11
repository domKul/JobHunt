package com.jobhunt.inftrastructure.userloginandregister.error;

import org.springframework.http.HttpStatus;

public record TokenErrorResponse(String message,
                                 HttpStatus status) {
}
