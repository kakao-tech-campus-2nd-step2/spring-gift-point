package gift.Model;

public class MessageDTO {
    private String object_type;
    private String text;
    private Link link;

    public String getObject_type() {
        return object_type;
    }

    public String getText() {
        return text;
    }

    public Link getLink() {
        return link;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
