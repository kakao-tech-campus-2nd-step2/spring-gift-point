package gift.dto;

import lombok.Data;

@Data
public class KakaoInfo {

    private Long id;
    private String nickname;
    private String email;

    public KakaoInfo(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}
