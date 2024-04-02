package com.jobhunt.inftrastructure.apivalidation;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiValidationErrorResponse(List<String> messages,
                                         HttpStatus httpStatus) {
}
