package com.example.bookshop.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.InvalidatedToken;
import com.example.bookshop.Entity.User;
import com.example.bookshop.Entity.UserRole;
import com.example.bookshop.EntityDto.Reponse.AuthenticationReponse;
import com.example.bookshop.EntityDto.Reponse.IntrospectResponse;
import com.example.bookshop.EntityDto.Request.AuthenticationRequest;
import com.example.bookshop.EntityDto.Request.IntrospectRequest;
import com.example.bookshop.EntityDto.Request.LogoutRequest;
import com.example.bookshop.EntityDto.Request.RefreshRequest;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Repository.InvalidatedTokenRepository;
import com.example.bookshop.Repository.RoleRepository;
import com.example.bookshop.Repository.UserRepository;
import com.example.bookshop.Repository.UserRoleRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "AuthenticationService")
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRoleRepository userroleRepository;
    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;

    private static final String SIGNER_KEY = "Ybe1luuJZ5/VoPjmQqjLu7+kSo4MSKwQoiXCffxtWBmDksNV9JjimExq2Coo7cth";

    public AuthenticationReponse authenticate(AuthenticationRequest authenticationRequest) {
        if (!userRepository.existsByEmail(authenticationRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).get();
        boolean isAdmin = false;
        Optional<UserRole> userRole = userroleRepository.findByUser_idAndRole_id(user.getId(), Long.valueOf(1L));
        if (userRole.isPresent()) {
            isAdmin = true;
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(user);
        AuthenticationReponse authenticationReponse = new AuthenticationReponse();

        authenticationReponse.setLastName(user.getLastName());
        authenticationReponse.setAuthenticated(authenticated);
        authenticationReponse.setToken(token);
        authenticationReponse.setAdmin(isAdmin);
        log.info("User {} authenticated successfully", user.getEmail());
        return authenticationReponse;
    }

    public IntrospectResponse introspectToken(IntrospectRequest introspectRequest)
            throws JOSEException, ParseException {
        String token = introspectRequest.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        IntrospectResponse introspectResponse = new IntrospectResponse();
        introspectResponse.setValid(isValid);
        return introspectResponse;
    }

    private String generateToken(User user) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("huumanh2002")
                .issueTime(new Date())
                .expirationTime(
                        new Date(
                                Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    private String buildScope(User user) {
        List<UserRole> user_roles = userroleRepository.findAllByUser(user);
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!user_roles.isEmpty()) {
            user_roles.forEach(s -> stringJoiner.add(s.getRole().getName()));
        }
        return stringJoiner.toString();
    }

    public void logout(LogoutRequest logoutRequest) throws JOSEException, ParseException {

        var signToken = verifyToken(logoutRequest.getToken());
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jit);
        invalidatedToken.setExpiryTime(expiryTime);

        invalidatedTokenRepository.save(invalidatedToken);
        log.info("Token with JTI {} has been invalidated", jit);

    }

    public AuthenticationReponse refreshToken(RefreshRequest refreshRequest) throws ParseException, JOSEException {

        var signedJWT = verifyToken(refreshRequest.getToken());
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jit);
        invalidatedToken.setExpiryTime(expiryTime);

        invalidatedTokenRepository.save(invalidatedToken);

        var email = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        boolean isAdmin = false;
        Optional<UserRole> userRole = userroleRepository.findByUser_idAndRole_id(user.getId(), Long.valueOf(1L));
        if (userRole.isPresent()) {
            isAdmin = true;
        }
        String token = generateToken(user);
        AuthenticationReponse authenticationReponse = new AuthenticationReponse();
        authenticationReponse.setAuthenticated(true);
        authenticationReponse.setToken(token);
        authenticationReponse.setAdmin(isAdmin);

        log.info("Token for user {} has been refreshed", user.getEmail());
        return authenticationReponse;
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(jwsVerifier);
        boolean valid = verified && expiryTime.after(new Date());

        if (!valid) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }
}
