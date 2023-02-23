package prv.rcl.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统内部流通 用户信息
 */
public class SysUser implements UserDetails {

    private User user;


    public SysUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        assert user != null;
        List<URRelationship> relationships = user.getRelationships();
        if (relationships == null) {
            return new ArrayList<SimpleGrantedAuthority>();
        }
        return relationships.stream().map(URRelationship::getRole)
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        assert user != null;
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        assert user != null;
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        assert user != null;
        return user.getStatus();
    }

    @Override
    public boolean isAccountNonLocked() {
        assert user != null;
        return user.getStatus();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        assert user != null;
        return user.getStatus();
    }

    @Override
    public boolean isEnabled() {
        assert user != null;
        return user.getStatus();
    }
}
