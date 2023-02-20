package prv.rcl.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class Role extends PubColumn {

    @Column(
            name = "name",
            columnDefinition = "VARCHAR(100)",
            unique = true,
            nullable = false
    )
    private String name;

    @Column(
            name = "remark",
            columnDefinition = "VARCHAR(100)",
            insertable = false
    )
    private String remark;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
