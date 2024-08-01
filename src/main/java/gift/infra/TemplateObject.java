package gift.infra;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

@JsonFormat(shape = Shape.OBJECT)
public record TemplateObject(
        @JsonProperty("object_type")
        String objectType,
        @Length(min = 1, max = 200)
        String text,
        Link link) {
    /*
    web_url : PC버전 카카오톡에서 사용하는 웹 링크 URL
    mobile_web_url : 모바일 카카오톡에서 사용하는 웹 링크 URL
     */
    public record Link(
            @JsonProperty("web_url")
            String webUrl,
            @JsonProperty("mobile_web_url")
            String mobileWebUrl) {
    }
}
