package com.nota.charactercalculator.character.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    public static final String DT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DT_FORMAT);

    private static final String INCORRECTLY_MADE_REQUEST = "Incorrectly made request";

    @Getter
    @AllArgsConstructor
    private static class ErrorResponse {

        private String status;

        private String reason;

        private String message;

        private String timestamp;
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String message = getMethodArgumentNotValidException(e);
        log.error("[VALIDATION ERROR]: {}", message);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                INCORRECTLY_MADE_REQUEST,
                message,
                LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error("[REQUEST PARAMETER ERROR]: {}", e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                INCORRECTLY_MADE_REQUEST,
                e.getMessage(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException e) {
        String message = getConstraintViolationException(e);
        log.error("[VALIDATION ERROR]: {}", message);

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.name(),
                INCORRECTLY_MADE_REQUEST,
                message,
                LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowableException(final Throwable e) {
        log.error("[INTERNAL SERVER ERROR]: {}", e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Unhandled exception",
                e.getMessage(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER));
    }

    private String getMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("Field: %s, Error: %s, Actual length: %s",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue() != null
                                && !fieldError.getRejectedValue().toString().isBlank()
                                ? fieldError.getRejectedValue().toString().length() : 0))
                .collect(Collectors.joining(","));
    }

    private String getConstraintViolationException(final ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(message -> String.format("Request Param: text, Error: %s, Actual length: %d",
                        message.getMessage(),
                        message.getInvalidValue() != null
                                && !message.getInvalidValue().toString().isBlank()
                                ? message.getInvalidValue().toString().length() : 0))
                .collect(Collectors.joining(","));
    }
}
