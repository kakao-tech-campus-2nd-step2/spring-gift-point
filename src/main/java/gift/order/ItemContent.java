package gift.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemContent {
    @JsonProperty("title_image_url")
    String titleImageUrl;
    @JsonProperty("title_image_text")
    String titleImageText;
    @JsonProperty("title_image_category")
    String titleImageCategory;

    public ItemContent(String titleImageUrl, String titleImageText, String titleImageCategory) {
        this.titleImageUrl = titleImageUrl;
        this.titleImageText = titleImageText;
        this.titleImageCategory = titleImageCategory;
    }

}
