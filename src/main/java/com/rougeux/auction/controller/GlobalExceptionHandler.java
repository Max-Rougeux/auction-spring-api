package com.rougeux.auction.controller;

import com.rougeux.auction.exception.BusinessException;
import com.rougeux.auction.exception.InternalException;
import com.rougeux.auction.exception.NotFoundException;
import com.rougeux.auction.exception.UnauthorizedException;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.support.factory.ApiResponseFactory;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponseFactory factory;

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<String>> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                factory.error(ex.getCode(), ex.getMessage())
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<String>> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                factory.error(ex.getCode(), ex.getMessage())
        );
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                factory.error(ex.getCode(), ex.getMessage())
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                factory.error(ApiCodes.CD_UNAUTHENTICATED, ex.getMessage())
        );
    }


    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ApiResponse<Void>> handleInternalException(InternalException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                factory.error(ApiCodes.CD_INTERNAL_ERROR, ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                factory.error(ApiCodes.CD_BAD_REQUEST, "request.badRequest")
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                factory.error(ApiCodes.CD_BAD_REQUEST, "request.badRequest")
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                factory.error(ApiCodes.CD_NOT_FOUND, "request.notFound")
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                factory.error(ApiCodes.CD_BAD_REQUEST, "request.methodNotAllowed")
        );
    }

}
