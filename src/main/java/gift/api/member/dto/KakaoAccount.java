package gift.api.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccount(
    Boolean emailNeedsAgreement,
    Boolean isEmailValid,
    Boolean isEmailVerified,
    String email
) {}
