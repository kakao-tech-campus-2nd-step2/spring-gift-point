package gift.domain.dto.request.member;

import gift.domain.entity.KakaoOauthMember;
import gift.domain.entity.Member;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import jakarta.validation.constraints.NotNull;

public class KakaoOauthMemberRequest extends MemberRequest {

    @NotNull
    protected final String accessToken;

    public KakaoOauthMemberRequest(String email, String accessToken) {
        super(Type.KAKAO, email);
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public KakaoOauthMember toEntity(Member member, Long kakaoIdentifier) {
        return new KakaoOauthMember(kakaoIdentifier, accessToken, member);
    }
}
