package prv.rcl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
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
     *
     * @param http SecurityFilterChain 构造器
     * @return securityFilterChain
     * @throws Exception 配置失败
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            UserDetailsService userDetailsService,
                                            AuthenticationManagerBuilder builder) throws Exception {
        http.authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated();

        http.cors().disable();

        http.csrf().disable();

        http.formLogin()
                .loginProcessingUrl("/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    setResponseType(response);
                    ResponseEntity<String> body = ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("登录成功!");
                    String res = new ObjectMapper().writeValueAsString(body);
                    response.getWriter().write(res);
                });
        http.exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    setResponseType(response);
                    ResponseEntity<String> body = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("未登录！");
                    String res = new ObjectMapper().writeValueAsString(body);
                    response.getWriter().write(res);
                });
        http.logout()
                .clearAuthentication(true)
                .logoutSuccessHandler((request, response, authentication) -> {
//                    setResponseType(response);
                    ResponseEntity<String> body = ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("注销成功!");
                    String s = new ObjectMapper().writeValueAsString(body);
                    response.getWriter().write(s);
                }).logoutRequestMatcher(new OrRequestMatcher(Arrays.asList(
                        new AntPathRequestMatcher("/logout", "POST"),
                        new AntPathRequestMatcher("/logout", "GET"),
                        new AntPathRequestMatcher("/exit", "GET")
                )));
        http.userDetailsService(userDetailsService);
        CustomerAuthenticationFilter cusF = customerAuthenticationFilter(builder);
        http.addFilterBefore(cusF, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    CustomerAuthenticationFilter customerAuthenticationFilter(
            AuthenticationManagerBuilder builder) {
        CustomerAuthenticationFilter filter = new CustomerAuthenticationFilter();
        filter.setAuthenticationManager(builder.getObject());
        return filter;
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            // 不会进入 过滤器链中
            web.ignoring().antMatchers("/resource/**");
            // 开启 debugger
            web.debug(true);
        };
    }

    public void setResponseType(HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

}
