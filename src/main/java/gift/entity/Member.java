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

    protected Member() {
    }

    public Member(String email, String password, String accountType, String name, String role) {
        validateEmail(email);
        validatePassword(password);
        validateAccountType(accountType);

        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.name = name;
        this.role = role;
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

    public String getAccessToken() { return accessToken;}
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(email, member.email)
                && Objects.equals(password, member.password) && Objects.equals(
                accountType, member.accountType) && Objects.equals(name, member.name)
                && Objects.equals(role, member.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, accountType, name, role);
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
}