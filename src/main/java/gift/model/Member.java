package gift.model;

import gift.exception.InvalidInputValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "points")
    private Long points;

    protected Member() {
    }

    public Member(Long id, String email, String password, String role, Long points) {
        validateEmail(email);
        validatePassword(password);
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Long getPoints() {
        return points;
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputValueException("이메일을 입력하세요.");
        }
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new InvalidInputValueException("유효한 이메일을 입력하세요.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputValueException("비밀 번호를 입력하세요.");
        }
    }

    public void addPoints(Long points) {
        this.points += points;
    }

    public void subtractPoints(Long price) {
        if (this.points < price) {
            throw new InvalidInputValueException("포인트가 부족합니다.");
        }
        this.points -= price;
    }

}
