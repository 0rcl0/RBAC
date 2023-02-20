package prv.rcl;

import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import prv.rcl.dao.RoleDao;
import prv.rcl.dao.UserDao;
import prv.rcl.dao.UserRoleDao;
import prv.rcl.entity.Role;
import prv.rcl.entity.URRelationship;
import prv.rcl.entity.User;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class RbacApplicationTests {


    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;

    @Autowired
    public RbacApplicationTests(UserDao userDao,RoleDao roleDao,UserRoleDao userRoleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void test() {
        User user = new User();
        user.setName("rcl111222");
        user.setMobile("188888888");
        user.setPassword("{noop}123456");
        user.setStatus(Boolean.TRUE);
        userDao.save(user);

        Role role = new Role();
        role.setName("admin");
        role.setRemark("god of this system master");
        roleDao.save(role);

        Role common = new Role();
        common.setName("common");
        common.setRemark("a ant of this system");
        roleDao.save(common);

        URRelationship urRelationship = new URRelationship();
        urRelationship.setUser(user);
        urRelationship.setRole(role);
        userRoleDao.save(urRelationship);

        URRelationship relationship = new URRelationship();
        relationship.setUser(user);
        relationship.setRole(common);
        userRoleDao.save(relationship);

        List<User> users = userDao.findAll();

        users.forEach(System.out::println);

        users.stream().flatMap(u -> u.getRelationships().stream())
                .map(URRelationship::getRole)
                .forEach(System.out::println);
    }

    @Test
    void test1()
    {
        List<URRelationship> allByUserId = userRoleDao.findAllByUser_Id(1L);
        Optional<User> optionalUser = userDao.findById(1L);
    }
}