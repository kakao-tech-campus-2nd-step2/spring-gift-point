package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.KakaoProperties;
import gift.KakaoWebClient;
import gift.api.OrderRequest;
import gift.api.OrderResponse;
import gift.dto.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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