package gift.dto;

import java.util.List;

public class MemberResponse {
    private Long id;
    private Long kakaoId;
    private String nickname;
    private String kakaoToken;
    private List<WishResponse> wishes;

    public MemberResponse(Long id, Long kakaoId, String nickname, String kakaoToken, List<WishResponse> wishes) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.kakaoToken = kakaoToken;
        this.wishes = wishes;
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

    public String getKakaoToken() {
        return kakaoToken;
    }

    public List<WishResponse> getWishes() {
        return wishes;
    }
}
