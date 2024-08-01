package gift.domain;

import java.util.Map;

public class OrderBodyDto {
    private String object_type;
    private String text;
    private Map<String, Object> link;
    private String button_title;

    public OrderBodyDto(String object_type, String text, Map<String, Object> link, String button_title) {
        this.object_type = object_type;
        this.text = text;
        this.link = link;
        this.button_title = button_title;
    }

    public String getObject_type() {
        return object_type;
    }

    public String getText() {
        return text;
    }

    public Map<String, Object> getLink() {
        return link;
    }

    public String getButton_title() {
        return button_title;
    }

}
