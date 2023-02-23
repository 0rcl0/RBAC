package prv.rcl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import prv.rcl.entity.SysUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtils {

    @Value("${jwt.issuer}")
    private String ISSUER; // project-name

    @Value("${jwt.audience}")
    private String AUDIENCE; // web

    @Value("${jwt.header}")
    private String AUTH_HEADER;

    private SignatureAlgorithm alg = SignatureAlgorithm.HS256;

    private long EXPIRE_TIME = 3 * 24 * 60 * 60 * 1000;

//    private final String SECRET = ISSUER;

    public String generateToken(SysUser sysUser) throws JsonProcessingException {
        String userJson = new ObjectMapper().writeValueAsString(sysUser.getUser());
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", userJson);
        return generateToken(map);
    }

    public String generateToken(Map<String, Object> claimMaps) {

        long now = System.currentTimeMillis();
        Date expireDate = new Date(now + EXPIRE_TIME);
        return Jwts.builder()
                // 设置主体 说明
                .setSubject("system")
                // 设置 TOKEN 生成时间
                .setIssuedAt(new Date(now))
                // 设置 TOKEN ID
                .setId(UUID.randomUUID().toString())
                // 数据压缩方式
                .compressWith(CompressionCodecs.GZIP)
                // 设置 TOKEN 失效时间
                .setExpiration(expireDate)
                .addClaims(claimMaps)
                // 设置加密方式，解密密码
                .signWith(alg, ISSUER)
                .compact();
    }

    public boolean verity(String token) {
        Claims claims = getAllClaimsFromToken(token);
        if (null == claims) {
            return false;
        }
        if (null == claims.getExpiration()) {
            return true;
        } else {
            Date expiration = claims.getExpiration();
            Date date = new Date();
            return date.before(expiration);
        }
    }

    public String getSubjectFormToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public String getClaimFormToken(String token, String key) {
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get(key);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(ISSUER)
                .parseClaimsJws(token)
                .getBody();
    }
}
