package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String accessToken;

    @Min(value = 0)
    private Integer point;

    protected Member() {
    }

    @PrePersist
    private void prePersist() {
        this.point = this.point == null ? 0 : this.point;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password, String accessToken) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getPoint() {
        return point;
    }

    public void subtract(Integer requestPoint) {
        this.point -= requestPoint;
    }

    public void add(Integer requestPoint) {
        this.point += requestPoint;
    }
}