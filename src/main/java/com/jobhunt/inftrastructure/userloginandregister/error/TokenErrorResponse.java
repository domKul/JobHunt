package com.jobhunt.inftrastructure.userloginandregister.error;

import org.springframework.http.HttpStatus;

record TokenErrorResponse(String message,
                          HttpStatus status) {
}
