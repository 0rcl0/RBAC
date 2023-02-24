package prv.rcl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import prv.rcl.dao.UserDao;
import prv.rcl.entity.SysUser;
import prv.rcl.entity.User;
import prv.rcl.service.UserService;
import prv.rcl.utils.VerifyUtils;

import java.util.Optional;

/**
 * 用户登录服务实现，
 * 用户密码更新服务实现。
 * {@code @Service} 注入容器
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService, UserDetailsPasswordService {

    private final UserDao userDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public <S extends User> S save(S entity) {
        LOGGER.info("非空校验!");
        return userDao.save(entity);
    }

    @Override
    public void delete(User entity) {
        LOGGER.info("delete user:{}", entity.getId());
        userDao.delete(entity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        SysUser sysUser = (SysUser) user;
        User us = sysUser.getUser();
//        boolean hasAdminRole = us.getRelationships().stream()
//                .map(URRelationship::getRole)
//                .map(Role::getName)
//                .anyMatch(s -> s.equalsIgnoreCase("ADMIN"));
//        // 不执行 update Password 操作
//        if(hasAdminRole) {
//            return sysUser;
//        }else{
        us.setPassword(newPassword);
        LOGGER.debug("user{} update password", us.getId());
        userDao.save(us);
        return sysUser;
//        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据 load 的 用户名判断是邮箱登录还是用户名登录
        boolean isEmailLogin = VerifyUtils.verifyEmail(username);
        if (isEmailLogin) {
            return findUserByEmail(username);
        } else {
            return findUserByUsername(username);
        }
    }

    public SysUser findUserByEmail(String email) {
        return userDao.findByEmail(email)
                .map(SysUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("邮箱:" + email + "未录入!"));
    }

    public SysUser findUserByUsername(String username) {
        return userDao.findByName(username)
                .map(SysUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("用户:" + username + "未找到!"));
    }

    public User addRole(Long roleId) {
        return null;
    }
}
