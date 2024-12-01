package com.team4.SmartHabitat.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<?> handleInternalServerError(InternalServerErrorException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getFieldError() != null
				? ex.getBindingResult().getFieldError().getDefaultMessage()
				: "Invalid input data";

		log.error("[BAD_REQUEST]: {}", errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}
	
	 @ExceptionHandler(HttpMessageNotReadableException.class)
	    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
	        log.error("[BAD_REQUEST]: Request body is missing or unreadable: {}", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request body is missing or unreadable.");
	    }

}
