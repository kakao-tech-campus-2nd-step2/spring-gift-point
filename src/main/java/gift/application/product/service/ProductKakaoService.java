package gift.application.product.service;

import gift.application.product.service.apiCaller.ProductKakaoApiCaller;
import gift.application.token.TokenManager;
import gift.model.token.KakaoToken;
import org.springframework.stereotype.Service;

@Service
public class ProductKakaoService {

    private final ProductKakaoApiCaller productKakaoApiCaller;
    private final TokenManager tokenManager;

    public ProductKakaoService(ProductKakaoApiCaller productKakaoApiCaller,
        TokenManager tokenManager) {
        this.productKakaoApiCaller = productKakaoApiCaller;
        this.tokenManager = tokenManager;
    }

    public void sendPurchaseMessage(Long memberId, String optionName) {
        KakaoToken token = tokenManager.getToken(memberId);
        productKakaoApiCaller.sendMessage(token.getAccessToken(), optionName);
    }

}
