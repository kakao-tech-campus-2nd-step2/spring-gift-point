package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 사용자 정보 DTO")
public class KakaoUserDTO {

    @Schema(description = "사용자 ID", example = "12345")
    private Long id;

    @Schema(description = "사용자 속성")
    private Properties properties;

    public static class Properties {

        @Schema(description = "사용자 닉네임", example = "홍길동")
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    public Long getId() {
        return id;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
