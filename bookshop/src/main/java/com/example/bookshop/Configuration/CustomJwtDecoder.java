package com.example.bookshop.Configuration;

import java.text.ParseException;
import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.example.bookshop.EntityDto.Request.IntrospectRequest;
import com.example.bookshop.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    private static final String SIGNER_KEY = "Ybe1luuJZ5/VoPjmQqjLu7+kSo4MSKwQoiXCffxtWBmDksNV9JjimExq2Coo7cth";

    @Autowired
    AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            IntrospectRequest introspectRequest = new IntrospectRequest();
            introspectRequest.setToken(token);
            if(!authenticationService.introspectToken(introspectRequest).isValid()){
                throw new JwtException("Token invalid");
            };
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }

}

