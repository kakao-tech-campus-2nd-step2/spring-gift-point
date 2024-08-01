package gift.entity;

import jakarta.persistence.*;

@Entity
public class KakaoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    Long kakaoUserId;

    @OneToOne(fetch = FetchType.LAZY)
    User user;

    public KakaoUser() {
    }

    public KakaoUser(Long kakaoUserId, User user) {
        this.kakaoUserId = kakaoUserId;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public Long getKakaoUserId() {
        return kakaoUserId;
    }

    public User getUser() {
        return user;
    }
}
