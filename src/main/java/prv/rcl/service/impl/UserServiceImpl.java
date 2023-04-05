package prv.rcl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import prv.rcl.dao.RoleDao;
import prv.rcl.dao.UserDao;
import prv.rcl.dao.UserRoleDao;
import prv.rcl.entity.Role;
import prv.rcl.entity.SysUser;
import prv.rcl.entity.URRelationship;
import prv.rcl.entity.User;
import prv.rcl.service.UserService;
import sun.security.util.Password;

import java.util.Optional;

/**
 * 用户登录服务实现，
 * 用户密码更新服务实现。
 * {@code @Service} 注入容器
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService, UserDetailsPasswordService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final RoleDao roleDao;

    private final UserRoleDao userRoleDao;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    public UserServiceImpl(UserDao userDao,PasswordEncoder passwordEncoder,RoleDao roleDao,UserRoleDao userRoleDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
    }

    @Override
    public <S extends User> S save(S entity) {
        LOGGER.info("非空校验!");
        //设置加密密码
        String password = entity.getPassword();
        if(StringUtils.hasText(password)) {
            entity.setPassword(passwordEncoder.encode(password));
        }
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

    /**
     * 登陆成功后密码自动升级
     * @param user the user to modify the password for
     * @param newPassword the password to change to, encoded by the configured
     * {@code PasswordEncoder}
     * @return {@link SysUser} 系统用户
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        SysUser sysUser = (SysUser) user;
        User us = sysUser.getUser();
        us.setPassword(newPassword);
        LOGGER.debug("user{} update password", us.getId());
        userDao.save(us);
        return sysUser;
//        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户:" + username + "未找到!"));
        return new SysUser(user);
    }

}
