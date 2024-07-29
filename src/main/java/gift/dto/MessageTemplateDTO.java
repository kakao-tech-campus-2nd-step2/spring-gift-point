package gift.dto;

public class MessageTemplateDTO {

    private String object_type;
    private String text;
    private LinkDTO link;

    public MessageTemplateDTO(String object_type, String text, LinkDTO link) {
        this.object_type = object_type;
        this.text = text;
        this.link = link;
    }

    public LinkDTO getLink() {
        return link;
    }

    public String getObjectType() {
        return object_type;
    }

    public String getText() {
        return text;
    }
}
