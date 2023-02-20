package prv.rcl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import prv.rcl.entity.Role;

public interface RoleDao extends JpaRepository<Role,Long> {
}
