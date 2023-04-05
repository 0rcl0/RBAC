package prv.rcl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Optional;

/**
 * 用户角色中间表
 */
@Entity
@Table(name = "user_role")
@EntityListeners(value = AuditingEntityListener.class)
public class URRelationship extends PubColumn {

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    private Role role;

    public URRelationship() {
    }

    public URRelationship(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "URRelationship{" +
                "user=" + user.getId() +
                "role=" + role +
                '}';
    }
}
