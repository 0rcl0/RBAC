package prv.rcl.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.data.annotation.*;
import java.util.Optional;

/**
 * 插入修改信息时
 * {@link CreatedBy} 动态获取,
 * {@link CreatedDate},
 * {@link LastModifiedBy} 动态获取,
 * {@link LastModifiedDate}
 */
public class MyAuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
//        // 获取当前登录人信息 Security 中获取
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = (Long) authentication.getPrincipal();
//        // 从请求中获取到 session 中的 用户信息
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        assert attributes != null;
//        Long userId1 = (Long) attributes.getRequest().getSession().getAttribute("userId");
        return Optional.of(1L);
    }
}
