package com.br.movie.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(MultipartException.class)
	ResponseEntity<?> multipartExceptionHandler(HttpServletRequest request, Throwable ex) {
		HttpStatus status = getStatus(request);
		return ResponseEntity.status(status).body(ex.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	ResponseEntity<?> unexpectedExceptionHandler(HttpServletRequest request, Throwable ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Contact the support");
	}
	
	private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
	
}
