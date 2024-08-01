package gift.infra;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.hibernate.validator.constraints.Length;

@JsonFormat(shape = Shape.OBJECT)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TemplateObject(
        String objectType,
        @Length(min = 1, max = 200)
        String text,
        Link link) {
    /*
    web_url : PC버전 카카오톡에서 사용하는 웹 링크 URL
    mobile_web_url : 모바일 카카오톡에서 사용하는 웹 링크 URL
     */
    public record Link(
            String webUrl,
            String mobileWebUrl) {
    }
}
