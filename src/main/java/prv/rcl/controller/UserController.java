package prv.rcl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import prv.rcl.entity.User;
import prv.rcl.service.UserService;

@RestController
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(name = "/user")
    public User getUser(@RequestParam("userId")Long uid) {
        LOGGER.info("request attribute userId is {}",uid);
        return userService.findById(uid).orElse(null);
    }
}
