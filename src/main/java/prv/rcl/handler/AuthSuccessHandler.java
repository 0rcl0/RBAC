package prv.rcl.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import prv.rcl.entity.SysUser;
import prv.rcl.utils.JwtUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    public AuthSuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseUtils.setJsonUTF_8Response(response);
        HashMap<String, String> resMap = new HashMap<>();
        resMap.put("msg", "登录成功!");
        // TODO 生成 TOKEN 存放等等
        SysUser sysUser = (SysUser) authentication.getPrincipal();
        String token = jwtUtils.generateToken(sysUser);
        resMap.put("token", token);
//        jwtUtils.generateToke()
        ResponseEntity<HashMap<String, String>> body = ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resMap);
        String res = new ObjectMapper().writeValueAsString(body);
        response.getWriter().write(res);
    }
}
