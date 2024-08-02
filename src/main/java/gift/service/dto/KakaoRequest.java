package gift.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class KakaoRequest {
    private static final String DEFAULT_OBJECT_TYPE = "feed";
    private static final String DEFAULT_PROFILE_TEXT = "선물하기";
    private static final String DEFAULT_SUM_TEXT = "합계";
    private static final String DEFAULT_TITLE = "주문해주셔서 감사합니다.";
    private static final String DEFAULT_ITEM_TEXT = "가격";
    private static final String DEFAULT_ITEM_COUNT_TEXT = "개수";
    private static final String DEFAULT_POINT_TEXT = "사용 포인트";

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Feed (
            String objectType,
            Content content,
            ItemContent itemContent
    ){
        public static Feed from(OrderDto orders) {
            return new Feed(
                    DEFAULT_OBJECT_TYPE,
                    new Content(
                            DEFAULT_TITLE,
                            orders.description(),
                            null
                    ),
                    new ItemContent(
                            DEFAULT_PROFILE_TEXT,
                            orders.productName(),
                            orders.productImageUrl(),
                            orders.optionName(),
                            new Item[]{
                                    new Item(
                                            DEFAULT_ITEM_TEXT,
                                            orders.price() + "원"
                                    ),
                                    new Item(
                                            DEFAULT_ITEM_COUNT_TEXT,
                                            orders.quantity() + "개"
                                    ),
                                    new Item(
                                            DEFAULT_POINT_TEXT,
                                            orders.point() + "포인트"
                                    ),
                            },
                            DEFAULT_SUM_TEXT,
                            (orders.price() * orders.quantity()) + "원"
                    )
            );
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record Content(
            String title,
            String description,
            Link[] link
    ){}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record ItemContent(
            String profileText,
            String titleImageText,
            String titleImageUrl,
            String titleImageCategory,
            Item[] items,
            String sum,
            String sumOp
    ){}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record Item(
            String item,
            String itemOp
    ){}

    record Link() {}
}
