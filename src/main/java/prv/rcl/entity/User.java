package prv.rcl.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class User extends PubColumn {

    @Column(name = "name",
            columnDefinition = "VARCHAR(50) comment '用户名'",
            nullable = false,
            unique = true)
    private String name;

    @Column(name = "nick_name",
            columnDefinition = "VARCHAR(150) DEFAULT '一只小蜗牛' comment '昵称'",
            insertable = false)
    private String nickName;

    @Column(name = "avatar",
            columnDefinition = "VARCHAR(150) comment '头像'",
            insertable = false)
    private String avatar;

    @Column(name = "password",
            columnDefinition = "VARCHAR(100) comment '密码'",
            nullable = false)
    private String password;

    @Column(
            name = "email",
            columnDefinition = "VARCHAR(20) comment '邮箱'",
            insertable = false
    )
    private String email;

    @Column(
            name = "mobile",
            columnDefinition = "CHAR(11) comment '手机号码'",
            insertable = false
    )
    private String mobile;

    @Column(name = "status",
            columnDefinition = "tinyint default 1",
            insertable = false)
    private Boolean status;

    private Long deptId;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<URRelationship> relationships;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public List<URRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<URRelationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", deptId=" + deptId +
                '}';
    }
}
