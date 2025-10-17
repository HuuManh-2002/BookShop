package com.example.bookshop.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiReponse<String>> handlingRuntimeException(RuntimeException exception) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setCode(ErrorCode.UNCATEGORIRED_EXCEPTION.getCode());
        apiReponse.setMessage(ErrorCode.UNCATEGORIRED_EXCEPTION.getMessage());
        return ResponseEntity
                .status(ErrorCode.UNCATEGORIRED_EXCEPTION.getHttpStatusCode())
                .body(apiReponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiReponse<String>> handlingAppException(AppException exception) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        ErrorCode errorCode = exception.getErrorCode();
        apiReponse.setCode(errorCode.getCode());
        apiReponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiReponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiReponse<String>> handlingValidation(MethodArgumentNotValidException exception) {

        String enumKey = exception.getFieldError().getDefaultMessage();
        System.out.println(enumKey);
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setCode(errorCode.getCode());
        apiReponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiReponse);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    ResponseEntity<ApiReponse<String>> handlingNoResourceFoundException(NoResourceFoundException exception) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setCode(ErrorCode.RESOURCE_NOT_FOUND.getCode());
        apiReponse.setMessage(ErrorCode.RESOURCE_NOT_FOUND.getMessage());
        return ResponseEntity.badRequest().body(apiReponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiReponse<String>> handlingAccessDeniedException(AccessDeniedException exception) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
        apiReponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());
        return ResponseEntity.status(ErrorCode.UNAUTHENTICATED.getHttpStatusCode()).body(apiReponse);
    }
}
