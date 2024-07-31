package gift.dto;

public record TemplateObject(String object_type, String text, Link link) {
    public static class Link {
        public String web_url;

        public Link(String web_url) {
            this.web_url = web_url;
        }
    }
}
