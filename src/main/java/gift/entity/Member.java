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

    @OneToMany(mappedBy = "member")
    private List<Wish> wishes = new ArrayList<>();

    public Member() {}

    public Member(Long kakaoId, String nickname, String kakaoToken) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.kakaoToken = kakaoToken;
    }

    public Long getKakaoId() {
        return kakaoId;
    }

    public void setKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getKakaoToken() {
        return kakaoToken;
    }

    public void setKakaoToken(String kakaoToken) {
        this.kakaoToken = kakaoToken;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public void setWishes(List<Wish> wishes) {
        this.wishes = wishes;
    }
}
