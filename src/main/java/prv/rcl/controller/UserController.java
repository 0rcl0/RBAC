package prv.rcl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping(name = "/user/{userId}")
    public User getUser(@PathVariable("userId") Long uid) {
        LOGGER.info("request attribute userId is {}", uid);
        return userService.findById(uid).orElse(null);
    }
}
