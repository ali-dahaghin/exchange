package ir.iau.exchange.service;

import io.jsonwebtoken.Claims;

public interface JwtService {

    long JWT_TOKEN_VALIDITY_STAKEHOLDER = 60 * 60 * 24 * 30 * 6;

    String generateToken(String username, long tokenValidity);

    Claims verifyToken(String token);

}
