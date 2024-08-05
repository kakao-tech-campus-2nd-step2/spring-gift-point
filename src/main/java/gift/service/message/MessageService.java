package gift.service.message;

import gift.exception.EntityNotFoundException;
import gift.model.option.Option;
import gift.model.product.Product;
import gift.model.token.OAuthToken;
import gift.model.user.User;
import gift.repository.option.OptionRepository;
import gift.repository.product.ProductRepository;
import gift.repository.token.OAuthTokenRepository;
import gift.util.KakaoApiCaller;
import gift.util.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MessageService {
    private final OptionRepository optionRepository;

    private final ProductRepository productRepository;

    private final gift.repository.token.OAuthTokenRepository OAuthTokenRepository;

    private final KakaoApiCaller kakaoApiCaller;

    private final TokenUtil tokenUtil;

    public MessageService(OptionRepository optionRepository,
                          ProductRepository productRepository,
                          OAuthTokenRepository OAuthTokenRepository,
                          KakaoApiCaller kakaoApiCaller,
                          TokenUtil tokenUtil
    ) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
        this.OAuthTokenRepository = OAuthTokenRepository;
        this.kakaoApiCaller = kakaoApiCaller;
        this.tokenUtil = tokenUtil;
    }

    public void sendMessage(Long optionId, Long productId, int quantity, String customMessage, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("해당 옵션이 존재하지 않습니다,"));
        OAuthToken OAuthToken = OAuthTokenRepository.findByUser(user).orElseThrow(() -> new NoSuchElementException("사용자가 카카오토큰을 가지고있지않습니다!"));
        OAuthToken = tokenUtil.checkExpiredToken(OAuthToken);
        String message = String.format("상품 : %s\\n옵션 : %s\\n수량 : %s\\n메시지 : %s\\n주문이 완료되었습니다!"
                , product.getName(), option.getName(), quantity, customMessage);
        kakaoApiCaller.sendMessage(OAuthToken.getAccessToken(), message);
    }
}
