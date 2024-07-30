package gift.main.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.main.Exception.CustomException;
import gift.main.dto.OrderResponse;
import gift.main.dto.TextTemplate;
import org.springframework.http.HttpStatus;

public class TextTemplateFactory {

    public static String convertOrderResponseToTextTemplateJson(OrderResponse orderResponse) {
        ObjectMapper objectMapper = new ObjectMapper();

        String objectType = "text";
        String wep_url = "http://localhost:8080/spring-gift";
        String text = "안녕하세요! " + orderResponse.buyerName() + "님!! 스프링 기프트 샵입니다!\n\n" +
                "저희 " + orderResponse.productName() + "을 구매해주셔서 감사합니다.🙇‍♀️\n" +
                "요청해주신 내용 " + orderResponse.message() + " 잘 확인했습니다~!\n\n" +
                "빠른 시일내에 배송해드리겠습니다^_^ \n\n" +
                "늘 좋은 날 보내시길 바랍니다~!\n\n" +
                "> 상품명 : " + orderResponse.productName() + "\n" +
                "> 옵션명 : " + orderResponse.optionName() + "\n" +
                "> 수량 개수 : " + orderResponse.quantity() + "\n" +
                "> 요청사항 : " + orderResponse.message();

        TextTemplate textTemplate = new TextTemplate(objectType, text, wep_url);

        String templateObjectJson = null;
        try {
            templateObjectJson = objectMapper.writeValueAsString(textTemplate);
        } catch (JsonProcessingException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return templateObjectJson;
    }


}
