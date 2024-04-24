package com.jamesou.dailycost.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jamesou
 * @Date: 6/04/24 23:11
 */
public class TokenUtil {

    /**
     * 设置延期的时间
     */
    private static final long EXPIR_DATE = 360000;

    /**
     * 设置token的秘钥
     */
    private static final String TOKEN_SECRET = "b7f2a1ed-c29f-48fc-b13e-5089938bbff4";


    /**
     * 获取token
     * @param userName
     * @param passWord
     * @return
     */
    public static String getTokenSecret(String userName, String passWord){
        String token = "";
        try {
            // 过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIR_DATE);
            // 设置头部信息
            Map<String, Object> head = new HashMap<>();
            head.put("typ", "JWT");
            head.put("alg", "HS256");

            // 设置秘钥
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

            // 生成签名
            token = JWT.create()
                    .withHeader(head)
                    .withClaim("userName", userName)
                    .withClaim("passWord", passWord)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return token;
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public static DecodedJWT verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}