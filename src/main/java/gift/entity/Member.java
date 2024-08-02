package gift.entity;

import gift.exception.BadRequestExceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class Member {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String accountType;

    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Size(max = 255)
    @Column(nullable = false)
    private String role;

    @Column(nullable = true)
    private String accessToken;

    @Column(nullable = false)
    private Long point;

    protected Member() { }

    public Member(String email, String password, String accountType, String name, String role) {
        validateEmail(email);
        validatePassword(password);
        validateAccountType(accountType);

        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public Member(String email, String password, String accountType, String name, String role, Long point) {
        this(email, password, accountType, name, role);
        validatePoint(point);
        this.point = point;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getAccountType() { return accountType; }
    public String getName() {
        return name;
    }
    public String getRole() {
        return role;
    }
    public Long getId() {
        return id;
    }
    public Long getPoint() { return point; }

    public String getAccessToken() { return accessToken;}
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public void addPoint(Long point) {
        validatePoint(point);
        this.point += point;
    }

    public void substractPoint(Long point) {
        validatePoint(point);
        if (point > this.point)
            throw new BadRequestException("포인트가 부족합니다.");
        this.point -= point;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Member member = (Member) object;
        return Objects.equals(getId(), member.getId()) && Objects.equals(getEmail(),
                member.getEmail()) && Objects.equals(getPassword(), member.getPassword())
                && Objects.equals(getAccountType(), member.getAccountType())
                && Objects.equals(getName(), member.getName()) && Objects.equals(
                getRole(), member.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword(), getAccountType(), getName(),
                getRole());
    }

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException("올바른 이메일 형식이 아닙니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("비밀번호를 입력해주세요.");
        }
    }

    private void validateAccountType(String accountType) {
        if(!Objects.equals(accountType, "basic") && !Objects.equals(accountType, "social"))
            throw new BadRequestException("계정 타입은 'basic' 또는 'social'이어야 합니다.");
    }

    private void validatePoint(Long point) {
        if(point == null || point < 0)
            throw new BadRequestException("올바르지 않은 포인트 입니다.");
    }
}