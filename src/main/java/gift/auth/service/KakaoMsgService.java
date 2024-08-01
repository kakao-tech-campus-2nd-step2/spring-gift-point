package gift.auth.service;

import gift.order.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class KakaoMsgService {


    public void sendMsg(String token, OrderRequest orderRequest) {

        WebClient webClient = WebClient.create();

        webClient.post().uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/x-www-form-urlencoded").bodyValue(
                "template_object={\"object_type\":\"text\",\"text\":\"" + orderRequest.getMsg()
                +"\",\"link\":{\"web_url\":\"http://localhost:8080\",\"mobile_web_url\":\"http://localhost:8080\"}}")
            .retrieve().bodyToMono(String.class).block();

    }
}
