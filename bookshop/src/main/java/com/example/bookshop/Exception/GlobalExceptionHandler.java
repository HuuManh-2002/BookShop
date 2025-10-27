package com.example.bookshop.Exception;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiReponse<String>> handlingException(Exception exception) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiReponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatusCode())
                .body(apiReponse);
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    ResponseEntity<ApiReponse<String>> handlingHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException exception) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setCode(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode());
        apiReponse.setMessage(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessage());
        return ResponseEntity
                .status(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatusCode())
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

    @ExceptionHandler(value = IOException.class)
    ResponseEntity<ApiReponse<String>> handlingIOException(IOException exception) {
        ApiReponse<String> apiReponse = new ApiReponse<>();
        apiReponse.setCode(ErrorCode.FILE_UPLOAD_FAILED.getCode());
        apiReponse.setMessage(ErrorCode.FILE_UPLOAD_FAILED.getMessage());
        return ResponseEntity.status(ErrorCode.FILE_UPLOAD_FAILED.getHttpStatusCode()).body(apiReponse);
    }
}
