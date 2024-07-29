package gift.dto;

import java.util.Map;

public class KakaoTextMessageRequestDto {
    private String object_type ;
    private String text ;
    private Map<String,String> link;

    public KakaoTextMessageRequestDto(String object_type, String text, Map<String, String> link) {
        this.object_type = object_type;
        this.text = text;
        this.link = link;
    }
}
