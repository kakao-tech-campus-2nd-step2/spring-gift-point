package gift.main.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.main.Exception.CustomException;
import gift.main.dto.OrderResponce;
import gift.main.dto.TextTemplate;
import org.springframework.http.HttpStatus;

public class TextTemplateFactory {

    public static TextTemplate makeTextTemplate(OrderResponce orderResponce) {

        String objectType = "text";
        String wep_url = "http://localhost:8080/spring-gift";
        String text = "안녕하세요! " + orderResponce.buyerName() + "님!! 스프링 기프트 샵입니다!\n\n" +
                "저희 " + orderResponce.productName() + "을 구매해주셔서 감사합니다.🙇‍♀️\n" +
                "요청해주신 내용 " + orderResponce.message() + " 잘 확인했습니다~!\n\n" +
                "빠른 시일내에 배송해드리겠습니다^_^ \n\n" +
                "늘 좋은 날 보내시길 바랍니다~!\n\n" +
                "> 상품명 : " + orderResponce.productName() + "\n" +
                "> 옵션명 : " + orderResponce.optionName() + "\n" +
                "> 수량 개수 : " + orderResponce.quantity() + "\n" +
                "> 요청사항 : " + orderResponce.message();

        return new TextTemplate(objectType, text, wep_url);
    }

    public static String convertOrderResponseToTextTemplateJson(OrderResponce orderResponce) {
        ObjectMapper objectMapper = new ObjectMapper();

        String objectType = "text";
        String wep_url = "http://localhost:8080/spring-gift";
        String text = "안녕하세요! " + orderResponce.buyerName() + "님!! 스프링 기프트 샵입니다!\n\n" +
                "저희 " + orderResponce.productName() + "을 구매해주셔서 감사합니다.🙇‍♀️\n" +
                "요청해주신 내용 " + orderResponce.message() + " 잘 확인했습니다~!\n\n" +
                "빠른 시일내에 배송해드리겠습니다^_^ \n\n" +
                "늘 좋은 날 보내시길 바랍니다~!\n\n" +
                "> 상품명 : " + orderResponce.productName() + "\n" +
                "> 옵션명 : " + orderResponce.optionName() + "\n" +
                "> 수량 개수 : " + orderResponce.quantity() + "\n" +
                "> 요청사항 : " + orderResponce.message();

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
