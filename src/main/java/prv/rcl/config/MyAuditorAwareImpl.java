package prv.rcl.config;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import prv.rcl.entity.SysUser;

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
        // 获取当前登录人信息 Security 中获取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        SysUser sysUser = (SysUser) authentication.getPrincipal();
//        // 从请求中获取到 session 中的 用户信息
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        assert attributes != null;
//        Long userId1 = (Long) attributes.getRequest().getSession().getAttribute("userId");
        return Optional.ofNullable(sysUser.getUser().getId());
    }
}
