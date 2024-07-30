package gift.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface KakaoMessageService {
    Mono<ResponseEntity<String>> sendMessage(String accessToken, String message);
}
