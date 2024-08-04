package gift.domain.member.entity;

import gift.exception.IllegalPointUseException;
import gift.exception.LackOfPointException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AuthProvider authProvider;

    @ColumnDefault("0")
    private int point = 0;

    protected Member() {

    }

    public Member(Long id, String name, String email, String password, Role role,
        AuthProvider authProvider) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authProvider = authProvider;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public int getPoint() {
        return point;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void rechargePoint(int amount) {
        if (amount <= 0) {
            throw new IllegalPointUseException("error.member.negative.point.value");
        }
        this.point += amount;
    }

    public void usePoint(int amount) {
        if (amount <= 0) {
            throw new IllegalPointUseException("error.member.negative.point.value");
        }
        if (this.point < amount) {
            throw new LackOfPointException("error.member.lack.of.point");
        }
        this.point -= amount;
    }
}
