package gift.oauth.message;

import static gift.exception.ErrorMessage.JSON_CONVERT_FAILED;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DefaultMessageTemplate {

    private String objectType;
    private String text;
    private Map<String, String> link;
    private String buttonTitle;

    public DefaultMessageTemplate(
        String objectType,
        String text,
        Map<String, String> link,
        String buttonTitle
    ) {
        this.objectType = objectType;
        this.text = text;
        this.link = link;
        this.buttonTitle = buttonTitle;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(JSON_CONVERT_FAILED);
        }
    }

    public String getObjectType() {
        return objectType;
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getLink() {
        return link;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }
}
