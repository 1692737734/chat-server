package com.chat.util.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Map;

/**
 * 这是一个生成token的工具类
 */
public class JavaWebToken {
    private static Logger log = LoggerFactory.getLogger(JavaWebToken.class);

    /**
     * 生成token
     * @param claims
     * @return
     */
    public static String createToken(Map<String,Object> claims){
        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
    }

    /**
     * 获取生成token的key
     * @return
     */
    private static Key getKeyInstance(){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("DyTvs");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static Map<String,Object> verifyToken(String token){
        try {
            Map<String, Object> jwtClaims =
                    Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token).getBody();
            return jwtClaims;
        } catch (Exception e) {
            log.error("解析Token错误 token:" + token);
            return null;
        }
    }
}
