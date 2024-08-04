package gift.main.dto;

import gift.main.entity.Role;
import jakarta.validation.constraints.NotBlank;

public record UserJoinRequest(
//        //여기서는 enum 타입의 에러를 어떻게 사용할지..?
//        String name,
        @NotBlank(message = "이메일을 적어주세요.")
        String email,
        @NotBlank(message = "패스워드 입력해주세요.")
        String password) {

    public UserJoinRequest(KakaoProfileRequest kakaoProfileRequest) {
        this(kakaoProfileRequest.email(), "kakaoPassword");
    }
}
