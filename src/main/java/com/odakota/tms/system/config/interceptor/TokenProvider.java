package com.odakota.tms.system.config.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.auth.Client;
import com.odakota.tms.enums.auth.TokenType;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class TokenProvider {

    private static final String SIGNING_KEY = "tms-app-signing-key";

    private static final String SUBJECT = "tms-authentication";

    private static final String ISSUER = "odakota";

    private final UserSession userSession;

    @Value("${auth.token.admin.access-expire}")
    private int adminAccessExpire;

    @Value("${auth.token.admin.refresh-expire}")
    private int adminRefreshExpire;

    @Value("${auth.token.customer.access-expire}")
    private int customerAccessExpire;

    @Value("${auth.token.customer.refresh-expire}")
    private int customerRefreshExpire;

    @Autowired
    public TokenProvider(UserSession userSession) {
        this.userSession = userSession;
    }

    public String generateToken(TokenType tokenType, Client client, String tokenId, Map<String, Object> data) {
        JWTCreator.Builder b = JWT.create().withSubject(SUBJECT).withIssuer(ISSUER)
                                  .withExpiresAt(new Date(generateTimeExpiration(tokenType, client)))
                                  .withJWTId(tokenId);
        if (tokenType.equals(TokenType.ACCESS)) {
            if (client.equals(Client.ADMIN)) {
                b.withClaim(Constant.TOKEN_CLAIM_USER_ID, (Long) data.get(Constant.TOKEN_CLAIM_USER_ID));
                b.withClaim(Constant.TOKEN_CLAIM_ROLE_ID, data.get(Constant.TOKEN_CLAIM_ROLE_ID).toString());
                if (data.get(Constant.TOKEN_CLAIM_BRANCH_ID) != null) {
                    b.withClaim(Constant.TOKEN_CLAIM_BRANCH_ID, (Long) data.get(Constant.TOKEN_CLAIM_BRANCH_ID));
                }
            } else {
                b.withClaim(Constant.TOKEN_CLAIM_CUSTOMER_ID, (Long) data.get(Constant.TOKEN_CLAIM_USER_ID));
            }
        }
        return b.sign(Algorithm.HMAC256(SIGNING_KEY.getBytes()));
    }

    public long generateTimeExpiration(TokenType tokenType, Client client) {
        Calendar calendar = Calendar.getInstance();
        if (tokenType.equals(TokenType.ACCESS)) {
            if (client.equals(Client.ADMIN)) {
                calendar.add(Calendar.MINUTE, adminAccessExpire);
            } else {
                calendar.add(Calendar.MINUTE, customerAccessExpire);
            }
        } else {
            if (client.equals(Client.ADMIN)) {
                calendar.add(Calendar.MINUTE, adminRefreshExpire);
            } else {
                calendar.add(Calendar.MINUTE, customerRefreshExpire);
            }
        }
        return calendar.getTimeInMillis();
    }

    void parseTokenInfoToUserSession(String token) throws UnAuthorizedException {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SIGNING_KEY.getBytes())).build();
            DecodedJWT jwt = jwtVerifier.verify(token);
            userSession.setUserId(jwt.getClaim(Constant.TOKEN_CLAIM_USER_ID).asLong());
            userSession.setRoleIds(Arrays.stream(jwt.getClaim(Constant.TOKEN_CLAIM_ROLE_ID).asString().split(",")).map(
                    Long::parseLong).collect(Collectors.toList()));
            userSession.setTokenId(jwt.getId());
            userSession.setBranchId(jwt.getClaim(Constant.TOKEN_CLAIM_BRANCH_ID).asLong());
        } catch (JWTDecodeException ex) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_INVALID, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            throw new UnAuthorizedException(MessageCode.MSG_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        }
    }
}
