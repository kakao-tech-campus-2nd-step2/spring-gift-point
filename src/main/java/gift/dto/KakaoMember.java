package gift.dto;

import gift.model.Member;

public record KakaoMember(KakaoProfile kakaoProfile, String password) {
    public String name() {
        return kakaoProfile.properties().nickname();
    }

    public String email() {
        return kakaoProfile.id().toString() + kakaoProfile.properties().nickname();
    }

    public String password() {
        return password;
    }

    public Member toMember() {
        return new Member(email(), password());
    }

}
