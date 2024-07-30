package gift.service;

import java.util.Map;

public class KakaoMessageTemplate {
    private String object_type;
    private String text;
    private Map<String, String> link;

    public KakaoMessageTemplate(String object_type, String text, Map<String, String> link) {
        this.object_type = object_type;
        this.text = text;
        this.link = link;
    }

    public String getObject_type() {
        return object_type;
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getLink() {
        return link;
    }
}