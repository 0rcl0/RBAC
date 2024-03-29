package prv.rcl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import prv.rcl.controller.CustomerAuthenticationFilter;
import prv.rcl.controller.JwtAuthenticationFilter;
import prv.rcl.handler.AccessDenyHandler;
import prv.rcl.handler.AuthFailureHandler;
import prv.rcl.handler.AuthSuccessHandler;
import prv.rcl.handler.JsonEntryPointHandler;
import prv.rcl.utils.JwtUtils;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
//开启全局方法权限
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * 注入 DelegatingPasswordEncoder
     *
     * @return passwordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 配置 过滤器链的构造配置
     * 根据注入的 AuthenticationManagerBuilder 配置 {@link CustomerAuthenticationFilter}
     *
     * @param http SecurityFilterChain 构造器
     * @return securityFilterChain
     * @throws Exception 配置失败
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            UserDetailsService userDetailsService,
                                            AuthenticationManagerBuilder builder,
                                            JwtUtils jwtUtils,
                                            AuthSuccessHandler authSuccessHandler) throws Exception {
        // 认证请求
        http.authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                // 解决resource 下添加了网站图标后网站不现实问题
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/actuator").permitAll()
                .anyRequest().authenticated();
        http.cors().disable();

        http.httpBasic();

        http.csrf().disable();

        http.formLogin()
                .loginProcessingUrl("/login")
                .successHandler(authSuccessHandler)
                .failureHandler(new AuthFailureHandler())
                .permitAll();

        http.exceptionHandling()
                .accessDeniedHandler(new AccessDenyHandler())
                .authenticationEntryPoint(new JsonEntryPointHandler());
        http.logout()
                .clearAuthentication(true)
                .logoutSuccessHandler((request, response, authentication) -> {
                    ResponseEntity<String> body = ResponseEntity
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("注销成功!");
                    String s = new ObjectMapper().writeValueAsString(body);
                    response.getWriter().write(s);
                }).logoutRequestMatcher(new OrRequestMatcher(Arrays.asList(
                        new AntPathRequestMatcher("/logout", "POST"),
                        new AntPathRequestMatcher("/logout", "GET"),
                        new AntPathRequestMatcher("/exit", "GET")
                )));
        http.userDetailsService(userDetailsService);


        // 过滤器添加
        CustomerAuthenticationFilter cusF = customerAuthenticationFilter(builder);
        cusF.setAuthenticationSuccessHandler(authSuccessHandler);
        JwtAuthenticationFilter authenticationFilter = jwtAuthenticationFilter(jwtUtils);
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(cusF, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 自定义认证过滤器  JSON 格式登录
     * @param builder 传人一个 builder 对象
     * @return 过滤器
     */
    CustomerAuthenticationFilter customerAuthenticationFilter(
            AuthenticationManagerBuilder builder) {
        CustomerAuthenticationFilter filter = new CustomerAuthenticationFilter();
        filter.setAuthenticationManager(builder.getObject());
        return filter;
    }

    /**
     * JWT 认证过滤器
     * @param jwtUtils 传人 jwtUtils
     * @return 过滤器
     */
    JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtils jwtUtils) {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setHeader("token");
        return jwtAuthenticationFilter;
    }


    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            // 开启 debugger
            web.debug(true);
        };
    }


}
