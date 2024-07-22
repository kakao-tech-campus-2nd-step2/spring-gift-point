package gift.product.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.exception.kakao.KakaoMessageException;
import gift.product.service.dto.ProductInfo;
import gift.product.service.dto.ProductOptionInfo;
import gift.product.service.dto.kakao.KakaoMessageTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record BuyProductMessageCommand(
        ProductInfo productInfo,
        ProductOptionInfo optionInfo,
        String accessToken,
        String message,
        Integer quantity
) {
    public static BuyProductMessageCommand of(ProductInfo productInfo, ProductOptionInfo optionInfo,
                                              String accessToken, String message, Integer quantity
    ) {
        return new BuyProductMessageCommand(productInfo, optionInfo, accessToken, message, quantity);
    }

    public MultiValueMap<String, String> toKakaoMessageTemplate() {
        var body = KakaoMessageTemplate.of(message, productInfo.name(), optionInfo.name(), quantity);

        MultiValueMap<String, String> templates = new LinkedMultiValueMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            templates.add("template_object", objectMapper.writeValueAsString(body));
        } catch (JsonProcessingException e) {
            throw new KakaoMessageException();
        }

        return templates;
    }
}
