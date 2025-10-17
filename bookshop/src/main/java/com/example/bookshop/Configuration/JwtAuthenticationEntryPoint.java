package com.example.bookshop.Configuration;

import java.io.IOException;
import java.rmi.ServerException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.example.bookshop.EntityDto.Reponse.ApiReponse;
import com.example.bookshop.Exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest httpServletRequest, 
        HttpServletResponse httpServletResponse, 
        AuthenticationException authenticationException) throws ServerException, IOException{
            ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
            httpServletResponse.setStatus(errorCode.getHttpStatusCode().value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ApiReponse<?> apiReponse = new ApiReponse<>();
            apiReponse.setCode(errorCode.getCode());
            apiReponse.setMessage(errorCode.getMessage());

            ObjectMapper objectMapper = new   ObjectMapper();


            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(apiReponse));
            httpServletResponse.flushBuffer();
    }

}
