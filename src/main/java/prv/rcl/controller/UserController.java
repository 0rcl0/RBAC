package prv.rcl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prv.rcl.entity.SysUser;
import prv.rcl.entity.User;
import prv.rcl.service.UserService;

import java.util.Optional;

@RestController
@SuppressWarnings("rawtypes")
public class UserController {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/{userId}",
            method = RequestMethod.GET
    )
    @PreAuthorize("hasRole('admin')")
    // 执行前判断是否有角色 admin
    public User getUser(@PathVariable Long userId) {
        LOGGER.info("request attribute userId is {}", userId);
        return userService.findById(userId).orElse(null);
    }

    /**
     * 获取当前登录人信息
     *
     * @return 登录人信息
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseEntity getUser() {
        SysUser loginUser = (SysUser) SecurityContextHolder.getContext().getAuthentication();
        User user = Optional.ofNullable(loginUser).flatMap(SysUser::getOpUser).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            LOGGER.debug("error is Security Context not authentication info");
            return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body("没有认证信息!");
        }
    }
    
}
