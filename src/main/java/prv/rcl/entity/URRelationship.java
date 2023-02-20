package prv.rcl.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "user_role")
@EntityListeners(value = AuditingEntityListener.class)
public class URRelationship extends PubColumn{

    @ManyToOne
    private User user;

    @ManyToOne
    private Role role;


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
}
