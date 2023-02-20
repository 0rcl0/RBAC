package prv.rcl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import prv.rcl.entity.User;

import java.util.Optional;

public interface UserDao extends JpaRepository<User,Long> {

    Optional<User> findByName(String name);

}
