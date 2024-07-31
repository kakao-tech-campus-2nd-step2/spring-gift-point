package gift.product.service.dto.kakao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = Shape.OBJECT)
public record KakaoMessageTemplate(
        @JsonProperty("object_type")
        String objectType,

        @JsonProperty("text")
        String text,

        @JsonFormat(shape = Shape.OBJECT)
        KakaoLinkTemplate link
) {
    public static KakaoMessageTemplate of(String textMessage, String productName, String optionName, Integer amount) {
        var kakaoMessage = textMessage.concat("\n\n")
                .concat(productName.concat("을 구매하였습니다.\n\n"))
                .concat("구매 상품 이름").concat(optionName + "\n")
                .concat(amount + "개를 구매하였습니다.");

        return new KakaoMessageTemplate("text", kakaoMessage, KakaoLinkTemplate.of(null));
    }
}