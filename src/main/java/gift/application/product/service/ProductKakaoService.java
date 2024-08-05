package gift.application.product.service;

import gift.application.product.service.apiCaller.ProductKakaoApiCaller;
import gift.application.token.TokenManager;
import gift.model.member.Member;
import gift.model.member.Provider;
import gift.model.token.KakaoToken;
import gift.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductKakaoService {

    private final ProductKakaoApiCaller productKakaoApiCaller;
    private final TokenManager tokenManager;
    private final MemberRepository memberRepository;

    public ProductKakaoService(
        ProductKakaoApiCaller productKakaoApiCaller,
        TokenManager tokenManager,
        MemberRepository memberRepository
    ) {
        this.productKakaoApiCaller = productKakaoApiCaller;
        this.tokenManager = tokenManager;
        this.memberRepository = memberRepository;
    }

    public void sendPurchaseMessage(Long memberId, String message) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        if (member.getProvider() != Provider.KAKAO) {
            return;
        }
        KakaoToken token = tokenManager.getToken(memberId);
        productKakaoApiCaller.sendMessage(token.getAccessToken(), message);
    }

}
