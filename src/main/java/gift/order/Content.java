package gift.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Content {
    @JsonProperty
    String title;
    @JsonProperty
    String description;
    @JsonProperty("image_url")
    String imageUrl;
    @JsonProperty
    Link link;


    public Content(String title, String description, String imageUrl, Link link) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.link = link;
    }

}
