package io.github.com.fafk.jwtauth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthConstants {

    public static String SECRET;
    public static long EXPIRATION_TIME;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/v1/user/sign-up";

    @Value("${application.security.tokenSecret:verySecretTokenSecret}")
    public void setSecret(String secret) { SECRET = secret; }

    @Value("${application.security.tokenExpiryInSeconds:2678400}")
    public void setExpirationTime(Long expiration) { EXPIRATION_TIME = expiration; }

}
