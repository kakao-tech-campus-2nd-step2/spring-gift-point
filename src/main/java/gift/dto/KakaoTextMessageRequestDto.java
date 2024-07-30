package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "카카오 메세지보내기 요청 DTO ")
public class KakaoTextMessageRequestDto {
    @Schema(description = "상품 타입")
    private String object_type;
    @Schema(description = "보내는 텍스트 메세지")
    private String text;
    @Schema(description = "상품 링크")
    private Map<String, String> link;

    public KakaoTextMessageRequestDto(String object_type, String text, Map<String, String> link) {
        this.object_type = object_type;
        this.text = text;
        this.link = link;
    }
}
