package gift.model.kakao;

public class TemplateObject {
    private String object_type;
    private String text;
    private LinkObject link;
    private String button_title;

    public TemplateObject() {
    }

    public TemplateObject(String object_type, String text, LinkObject link, String button_title) {
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

    public LinkObject getLink() {
        return link;
    }

    public String getButton_title() {
        return button_title;
    }
}
