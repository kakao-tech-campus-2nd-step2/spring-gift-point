package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import jakarta.persistence.*;

@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COMMENT 'User Email'")
    private String email;

    @Column(columnDefinition = "VARCHAR(255) COMMENT 'User Password'")
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private KakaoUser kakaoUser;

    protected User() {
    }

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this(null, email, password);
    }

    public User(KakaoUser kakaoUser) {
        this.kakaoUser = kakaoUser;
        this.kakaoUser.setUser(this);
    }

    public void update(String email, String password) {
        validateEmail(email);
        validatePassword(password);
        this.email = email;
        this.password = password;
    }

    private void validateEmail(String email) {
        if (email != null && (email.trim().isEmpty() || !email.contains("@"))) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL);
        }
    }

    private void validatePassword(String password) {
        if (password != null && password.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public boolean isPasswordCorrect(String password) {
        return this.password != null && this.password.equals(password);
    }

    public boolean isPasswordIncorrect(String password) {
        return !isPasswordCorrect(password);
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

    public KakaoUser getKakaoUser() {
        return kakaoUser;
    }

    public void setKakaoUser(KakaoUser kakaoUser) {
        this.kakaoUser = kakaoUser;
    }
}
