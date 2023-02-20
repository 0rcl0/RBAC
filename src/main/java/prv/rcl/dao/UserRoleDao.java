package prv.rcl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import prv.rcl.entity.URRelationship;

import java.util.List;

public interface UserRoleDao extends JpaRepository<URRelationship,Long> {

    List<URRelationship> findAllByUser_Id(Long userId);

}
