package gift.DTO.Kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Content {
    private String title;
    private String imageUrl;
    private String description;
    private Link link;

    public Content(String title, String imageUrl, String description, Link link) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public Link getLink() {
        return link;
    }
}
