package gift.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 로그인 모델 클래스
 */
public record MemberDTO(

        Long id,
        @NotNull(message = "email은 필수 입력입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
                message = "이메일 형식에 맞지 않습니다.")
        String email,

        @Size(min = 4, max = 30, message = "비밀번호는 4자 이상 30자 미만입니다.")
        @NotNull
        String password,

        String kakaoAccessToken
) {

    public MemberDTO(long id, String email, String password) {
        this(id, email, password, null);
    }

    public MemberDTO(String email, String password) {
        this(null, email, password, null);
    }

    public MemberDTO(String email, String password, String kakaoAccessToken) {
        this(null, email, password, kakaoAccessToken);
    }

    /**
     * 사용자 ID 반환 메서드
     *
     * @return 사용자 ID
     */
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
