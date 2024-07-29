package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kakao_user")
public class KakaoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, columnDefinition = "BIGINT COMMENT 'Kakao ID'")
    private Long kakaoId;

    @Column(columnDefinition = "VARCHAR(255) COMMENT 'Kakao Nickname'")
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BIGINT COMMENT 'User ID'")
    private User user;

    protected KakaoUser() {
    }

    public KakaoUser(Long kakaoId, String nickname, User user) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Long getKakaoId() {
        return kakaoId;
    }

    public String getNickname() {
        return nickname;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
