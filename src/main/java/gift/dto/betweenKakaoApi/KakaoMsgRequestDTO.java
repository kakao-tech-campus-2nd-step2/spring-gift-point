package gift.dto.betweenKakaoApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record KakaoMsgRequestDTO(
    @JsonProperty("object_type")
    String objectType,

    @JsonProperty("content")
    Content content,

    @JsonProperty("item_content")
    ItemContent itemContent
) {
    public record ItemContent(
        @JsonProperty("profile_text")
        String profileText, //주문해 주셔서 감사합니다.

        @JsonProperty("title_image_text")
        String productName, //제품 이름

        @JsonProperty("title_image_url")
        String productImageUrl, // 제품 이미지

        @JsonProperty("title_image_category")
        String optionName, //옵션 이름

        @JsonProperty("items")
        List<ItemInfo> priceList, //가격

        @JsonProperty("sum") //합계 제목
        String sum,

        @JsonProperty("sum_op")
        String sumOp
    ){ public record ItemInfo(
            @JsonProperty("item") String itemName,
            @JsonProperty("item_op") String price
    ) { }}

    public record Content(
        @JsonProperty("image_url")
        String imageUrl,

        @JsonProperty("link")
        Link link
    ){
        public record Link(@JsonProperty("web_url") String webUrl){ }
    }
}