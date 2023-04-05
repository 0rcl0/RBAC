package prv.rcl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prv.rcl.dao.RoleDao;
import prv.rcl.dao.UserDao;
import prv.rcl.dao.UserRoleDao;
import prv.rcl.entity.Role;
import prv.rcl.entity.URRelationship;
import prv.rcl.entity.User;
import prv.rcl.service.RoleService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    private final UserRoleDao userRoleDao;

    private final UserDao userDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, UserRoleDao userRoleDao, UserDao userDao) {
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.userDao = userDao;
    }

    @Override
    public <S extends Role> S save(S entity) {
        return roleDao.save(entity);
    }

    @Override
    public void delete(Role entity) {
        roleDao.delete(entity);
    }

    @Override
    public Optional<Role> findById(Long rid) {
        return roleDao.findById(rid);
    }

    /**
     * 保存一个 user 和 role 的关系
     * @param uid userid
     * @param rid roleId
     * @return 是否增加成功
     */
    @Override
    public boolean saveUserRole(Long uid, Long rid) {
        Optional<User> user = userDao.findById(uid);
        Optional<Role> role = roleDao.findById(rid);
        URRelationship urRelationship = user.flatMap(u -> role.map(r -> new URRelationship(u, r))).orElse(null);
        userRoleDao.save(urRelationship);
        return true;
    }

    @Override
    public boolean saveUserRoles(Long uid, Long[] rids) {
        Optional<User> user = userDao.findById(uid);
        List<Role> allRole = roleDao.findAllById(Arrays.asList(rids));
        // 构造 u-r 关系对象
        List<URRelationship> urRelationshipList = allRole.stream().map(role ->
                user.map(u -> new URRelationship(u, role))
        ).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        userRoleDao.saveAll(urRelationshipList);
        return true;
    }

}
