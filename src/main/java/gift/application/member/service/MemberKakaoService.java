package gift.application.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import gift.application.member.dto.MemberKakaoModel;
import gift.application.member.dto.OAuthCommand;
import gift.application.member.service.apicaller.MemberKakaoApiCaller;
import gift.application.token.TokenManager;
import org.springframework.stereotype.Service;

@Service
public class MemberKakaoService {

    private final MemberKakaoApiCaller memberKakaoApiCaller;
    private final TokenManager tokenManager;

    public MemberKakaoService(MemberKakaoApiCaller kaKaoMemberKakaoApiCaller,
        TokenManager tokenManager) {
        this.memberKakaoApiCaller = kaKaoMemberKakaoApiCaller;
        this.tokenManager = tokenManager;
    }

    /**
     * 토큰을 사용해서 사용자 정보를 가져오는 로직
     */
    public OAuthCommand.MemberInfo getMemberInfo(String accessToken) {
        MemberKakaoModel.MemberInfo memberInfo = memberKakaoApiCaller.getMemberInfo(accessToken);
        return OAuthCommand.MemberInfo.from(memberInfo);
    }
}
