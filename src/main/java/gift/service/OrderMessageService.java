package gift.service;

import gift.dto.KakaoProperties;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.OptionRepository;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageService {

    private final KakaoProperties kakaoProperties;
    private final KakaoApiClient kakaoApiClient;
    private final OptionRepository optionRepository;

    public OrderMessageService(KakaoProperties kakaoProperties, KakaoApiClient kakaoApiClient,
        OptionRepository optionRepository) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoApiClient = kakaoApiClient;
        this.optionRepository = optionRepository;
    }

    public void sendOrderMessage(Order order, String token) {
        String messageTemplate = createMessageTemplate(order);
        kakaoApiClient.sendOrderMessage(token, messageTemplate);
    }

    private String createMessageTemplate(Order order) {
        Option option = optionRepository.findById(order.getOptionId())
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = order.getOrderDateTime().format(formatter);

        return "{ " +
            "\"object_type\": \"text\", " +
            "\"text\": \"주문 내역\\n" +
            "옵션명: " + option.getName() + "\\n" +
            "주문 메세지: " + order.getMessage() + "\\n" +
            "주문 수량: " + order.getQuantity() + "\\n" +
            "주문 시각: " + formattedDateTime + "\\n" +
            "남은 수량: " + option.getStockQuantity() + "\", " +
            "\"link\": { " +
            "\"web_url\": \"" + kakaoProperties.getRedirectUrl() + order.getId() + "\" " +
            "} " +
            "}";
    }

}
