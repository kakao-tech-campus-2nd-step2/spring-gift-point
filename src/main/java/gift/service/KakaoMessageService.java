package gift.service;

import gift.KakaoWebClient;
import gift.api.OrderResponse;
import gift.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KakaoMessageService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoMessageService.class);
    private final KakaoWebClient kakaoWebClient;
    private final OptionService optionService;
    private final ProductService productService;

    public KakaoMessageService(KakaoWebClient kakaoWebClient, OptionService optionService, ProductService productService) {
        this.kakaoWebClient = kakaoWebClient;
        this.optionService = optionService;
        this.productService = productService;
    }

    public OrderResponse sendKakaoMessage(String accessToken, OrderDTO orderDTO) {
        String productName = productService.getProductNameById(optionService.getProductIdByOptionId(orderDTO.getOptionId()));
        String optionName = optionService.getOptionNameById(orderDTO.getOptionId());
        return kakaoWebClient.send(accessToken, orderDTO, productName, optionName);
    }
}