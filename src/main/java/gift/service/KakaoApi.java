package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.model.kakao.KakaoAuth;
import gift.model.kakao.KakaoMember;
import org.springframework.stereotype.Component;

@Component
public interface KakaoApi {
    KakaoAuth getKakaoToken(String code);
    KakaoMember getKakaoMemberId(String token);
    void sendKakaoMessage(String token, String message) throws JsonProcessingException;
}
