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

import java.util.Optional;
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
        LOGGER.info("delete user:{}",entity.getId());
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
        us.setPassword(newPassword);
        LOGGER.debug("user{} update password",us.getId());
        userDao.save(us);
        return sysUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户:" + username + "未找到!"));
        return new SysUser(user);
    }
}
