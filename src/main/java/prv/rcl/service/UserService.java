package prv.rcl.service;

import prv.rcl.entity.User;

public interface UserService extends PubService<User, Long> {

    User addRole(Long roleId);

}
