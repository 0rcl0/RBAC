package prv.rcl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import prv.rcl.entity.User;
import prv.rcl.service.UserService;

@RestController
public class UserController {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/{userId}",
            method = RequestMethod.GET
    )
    // 执行前判断是否有角色 admin
    @PreAuthorize("hasRole('admin')")
    public User getUser(@PathVariable Long userId) {
        LOGGER.info("request attribute userId is {}", userId);
        return userService.findById(userId).orElse(null);
    }

}
