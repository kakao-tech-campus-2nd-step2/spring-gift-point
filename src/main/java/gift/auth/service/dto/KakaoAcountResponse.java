package gift.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;

public record KakaoAcountResponse(
        @JsonProperty("has_email")
        @AssertTrue(message = "Kakao 계정에 Email이 존재하지 않습니다.")
        Boolean hasEmail,

        @JsonProperty("is_email_verified")
        @AssertTrue(message = "Kakao 계정의 Email이 유효하지 않습니다.")
        Boolean isEmailValid,
        
        @JsonProperty("is_email_valid")
        @AssertTrue(message = "Kakao 계정의 Email이 인증되지 않았습니다.")
        Boolean isEmailVerified,

        String email
) {
}
