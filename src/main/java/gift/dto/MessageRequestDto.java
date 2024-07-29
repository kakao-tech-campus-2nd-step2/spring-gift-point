package gift.dto;

import java.util.List;
import java.util.stream.Collectors;

public class MessageRequestDto {
    private String object_type;
    private String text;
    private Link link;

    public MessageRequestDto() {
        this.object_type = "text";
        this.link = new Link("https://developers.kakao.com");
    }

    public void setText(List<OrderResponseDto> orders) {
        this.text = orders.stream().map(order -> String.valueOf(order.getId()))
            .collect(Collectors.joining(", "));
    }

    public String getObject_type() {
        return object_type;
    }

    public String getText() {
        return text;
    }

    public Link getLink() {
        return link;
    }

    public static class Link {
        private String web_url;

        public Link(String web_url) {
            this.web_url = web_url;
        }

        public String getWeb_url() {
            return web_url;
        }
    }
}
