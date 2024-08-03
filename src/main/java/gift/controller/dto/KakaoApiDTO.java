package gift.controller.dto;

import java.time.LocalDateTime;

public class KakaoApiDTO {
    public record KakaoCode(String client_id,
                            String redirect_uri,
                            String response_type){}
    public record KakaoTokenRequset(String grant_type,
                                    String client_id,
                                    String redirect_uri,
                                    String code,
                                    String client_secret){}
    public record KakaoOrderRequest(Long optionId,
                                    int quantity,
                                    int point,
                                    String message){}
    public record KakaoOrderResponse(Long id,
                                     Long optionId,
                                     int quantity,
                                     LocalDateTime orderDateTime,
                                     String message){}
}
