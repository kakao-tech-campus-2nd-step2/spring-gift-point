package gift.oauth.business.service;

import gift.global.util.EncryptionUtils;
import gift.member.business.dto.JwtToken;
import gift.member.business.dto.MemberIn;
import gift.member.business.service.MemberService;
import gift.member.persistence.repository.MemberRepository;
import gift.oauth.business.client.OAuthApiClient;
import gift.oauth.business.dto.OAuthParam;
import gift.global.domain.OAuthProvider;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {
    private final Map<OAuthProvider, OAuthApiClient> clients;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public OAuthService(List<OAuthApiClient> clients, MemberRepository memberRepository,
        MemberService memberService) {
        this.clients = clients.stream().collect(
            Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity()));
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    @Transactional
    public JwtToken loginOrRegister(OAuthParam param) {
        OAuthApiClient client = clients.get(param.oAuthProvider());
        var oAuthToken = client.getOAuthToken(param);
        var email = client.getEmail(oAuthToken.accessToken(), param);
        if(memberRepository.existsByEmail(email)) {
            var memberInVendorLogin = new MemberIn.VendorLogin(
                email, param.oAuthProvider(),
                oAuthToken.accessToken(), oAuthToken.refreshToken());
            return memberService.loginVendorMember(memberInVendorLogin);
        }
        var memberInVendorRegister = new MemberIn.VendorRegister(email,  param.oAuthProvider(),
            oAuthToken.accessToken(), oAuthToken.refreshToken());
        return memberService.registerVendorMember(memberInVendorRegister);
    }
}
