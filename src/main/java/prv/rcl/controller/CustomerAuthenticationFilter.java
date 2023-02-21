package prv.rcl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 增加过滤器，POST  application-json 格式的请求才能被处理
 */
public class CustomerAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            logger.info("CustomerAuthenticationFilter return", new Throwable("Request method not support"));
            return null;
        } else if (!request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            logger.info("CustomerAuthenticationFilter return", new Throwable("not support Content-Type must be APPLICATION-JSON"));
            return null;
        }
        try {
            ServletInputStream inputStream = request.getInputStream();
            Map<String, String> map = new ObjectMapper().readValue(inputStream, Map.class);
            String username = map.get(getUsernameParameter());
            username = username != null ? username : "";
            String password = map.get(getPasswordParameter());
            password = password != null ? password : "";
            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<String> body = ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("登录成功!");
        String res = new ObjectMapper().writeValueAsString(body);
        response.getWriter().write(res);
    }
}
