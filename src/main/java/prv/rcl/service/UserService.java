package prv.rcl.service;

import prv.rcl.entity.User;

public interface UserService extends PubService<User, Long> {

    void addRole(Long uid, Long rid);

}
