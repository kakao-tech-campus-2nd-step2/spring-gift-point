package gift.model;

import gift.PasswordEncoder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(255) COMMENT '사용자 이메일'")
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) COMMENT '사용자 비밀번호'")
    private String password;

    @Column(nullable = false, columnDefinition = "INT COMMENT '사용자 포인트'")
    private int point; // 포인트 필드 추가

    protected User() {}

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = 0; // 포인트 기본값 설정
    }

    public User(Long id, String email, String password, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public boolean matchesPassword(String rawPassword) {
        return PasswordEncoder.encode(rawPassword).equals(this.password);
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

    public int getPoint() {
        return point;
    }

    public void addPoint(int amount) {
        this.point += amount;
    }

    public void subtractPoint(int amount) {
        if (this.point >= amount) {
            this.point -= amount;
        } if (this.point <= amount){
            this.point = 0;
        }
    }
}