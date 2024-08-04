package gift.member.domain;

import gift.auth.KakaoResponse;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

import static gift.member.domain.OauthProvider.KAKAO;
import static jakarta.persistence.EnumType.STRING;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = false)
    private int point = 0;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OauthProvider oauthProvider;

    @Column(nullable = true, unique = true, length = 255)
    private Long kakaoId;

    @Column(nullable = true)
    private String kakaoAccessToken;

    @Column(nullable = true)
    private String kakaoRefreshToken;

    @Column(nullable = true)
    private LocalDateTime kakaoAccessTokenExpiresAt;

    @Column(nullable = true)
    private LocalDateTime kakaoRefreshTokenExpiresAt;

    protected Member() {
    }

    public Member(String email, String password, OauthProvider oauthProvider) {
        this(null, email, password, oauthProvider, null);
    }

    public Member(Long id, String email, String password, OauthProvider oauthProvider) {
        this(id, email, password, oauthProvider, null);
    }

    public Member(Long id, String email, String password, OauthProvider oauthProvider, Long kakaoId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.oauthProvider = oauthProvider;
        this.kakaoId = kakaoId;
    }

    public Member(
            Long id,
            String email,
            OauthProvider oauthProvider,
            Long kakaoId,
            String kakaoAccessToken,
            String kakaoRefreshToken,
            LocalDateTime kakaoAccessTokenExpiresAt,
            LocalDateTime kakaoRefreshTokenExpiresAt
    ) {
        this.id = id;
        this.email = email;
        this.oauthProvider = oauthProvider;
        this.kakaoId = kakaoId;
        this.kakaoAccessToken = kakaoAccessToken;
        this.kakaoRefreshToken = kakaoRefreshToken;
        this.kakaoAccessTokenExpiresAt = kakaoAccessTokenExpiresAt;
        this.kakaoRefreshTokenExpiresAt = kakaoRefreshTokenExpiresAt;
    }

    public static Member fromKakao(KakaoResponse memberInfo) {
        return new Member(
                null,
                memberInfo.kakaoAccount().email(),
                null,
                KAKAO,
                memberInfo.id()
        );
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

    public Long getKakaoId() {
        return kakaoId;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public String getKakaoRefreshToken() {
        return kakaoRefreshToken;
    }

    public LocalDateTime getKakaoAccessTokenExpiresAt() {
        return kakaoAccessTokenExpiresAt;
    }

    public LocalDateTime getKakaoRefreshTokenExpiresAt() {
        return kakaoRefreshTokenExpiresAt;
    }

    public void update(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 기한이 30일 이상 남은 refresh token은 null을 반환하므로 null이 아닐 경우에만 갱신한다.
    public void updateKakaoTokens(
            String accessToken,
            String refreshToken,
            LocalDateTime kakaoAccessTokenExpiresAt,
            LocalDateTime kakaoRefreshTokenExpiresAt
    ) {
        this.kakaoAccessToken = accessToken;
        this.kakaoRefreshToken = refreshToken != null ? refreshToken : this.kakaoRefreshToken;
        this.kakaoAccessTokenExpiresAt = kakaoAccessTokenExpiresAt;
        this.kakaoRefreshTokenExpiresAt = kakaoRefreshTokenExpiresAt;
    }

    public void addPoint(int amount) {
        this.point += amount;
    }
}
