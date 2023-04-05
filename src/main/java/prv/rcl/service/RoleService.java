package prv.rcl.service;

import prv.rcl.entity.Role;

public interface RoleService extends PubService<Role,Long>{

    boolean saveUserRole(Long uid,Long rid);

    boolean saveUserRoles(Long uid,Long[] rids);

}
