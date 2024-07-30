package gift.member.model;

import gift.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Member extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name", length = 15)
    private String name;
    @Column(name = "role")
    private String role;

    protected Member() {

    }

    public Member(String email, String password, String name, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(Long id, String email, String name, String role) {
        this.setId(id);
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
