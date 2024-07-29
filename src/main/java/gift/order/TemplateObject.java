package gift.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateObject {
    @JsonProperty("object_type")
    String objectType;
    @JsonProperty
    Content content;
    @JsonProperty("item_content")
    ItemContent itemContent;

    public TemplateObject(String objectType, Content content, ItemContent itemContent) {
        this.objectType = objectType;
        this.content = content;
        this.itemContent = itemContent;
    }
}
