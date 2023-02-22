package prv.rcl.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import prv.rcl.entity.SysUser;
import prv.rcl.entity.User;
import prv.rcl.utils.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String FILTERED_ATT = "FILTER_TOKEN";

    private JwtUtils jwtUtils;
    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤器链
     * @throws ServletException 异常
     * @throws IOException      IO Exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(getHeader());
        if (authHeader == null) {
            request.setAttribute(getAlreadyFilteredAttributeName(), "FILTER");
            return;
        }

        if (jwtUtils.verity(authHeader)) {
            String value = jwtUtils.getClaimFormToken(authHeader, "user");
            try {
                User user = new ObjectMapper().readValue(value, User.class);
                SysUser sysUser = new SysUser(user);
                UsernamePasswordAuthenticationToken authenticated =
                        UsernamePasswordAuthenticationToken.authenticated(sysUser.getUsername(), sysUser.getPassword(), sysUser.getAuthorities());
                authenticated.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
                emptyContext.setAuthentication(authenticated);
                SecurityContextHolder.setContext(emptyContext);
                request.setAttribute(getAlreadyFilteredAttributeName(), "FILTER");
            } catch (JsonProcessingException e) {
                filterChain.doFilter(request, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected String getAlreadyFilteredAttributeName() {
        String name = getFilterName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + FILTERED_ATT;
    }
    
}
