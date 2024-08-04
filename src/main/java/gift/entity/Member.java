package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity {
    private Long kakaoId;
    private String nickname;
    private String kakaoToken;
    private int points;

    @OneToMany(mappedBy = "member")
    private List<Wish> wishes = new ArrayList<>();

    public Member() {}

    public Member(Long kakaoId, String nickname, String kakaoToken) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.kakaoToken = kakaoToken;
        this.points = 0;
    }

    public String getNickname() {
        return nickname;
    }

    public String getKakaoToken() {
        return kakaoToken;
    }

    public void setKakaoToken(String kakaoToken) {
        this.kakaoToken = kakaoToken;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void deductPoints(int points) {
        if (this.points < points) {
            throw new IllegalArgumentException("Insufficient points");
        }
        this.points -= points;
    }
}
