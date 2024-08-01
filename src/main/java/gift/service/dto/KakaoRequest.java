package gift.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.model.Orders;

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
        public static Feed from(Orders orders) {
            return new Feed(
                    DEFAULT_OBJECT_TYPE,
                    new Content(
                            DEFAULT_TITLE,
                            orders.getDescription(),
                            null
                    ),
                    new ItemContent(
                            DEFAULT_PROFILE_TEXT,
                            orders.getProductName(),
                            orders.getOptionName(),
                            new Item[]{
                                    new Item(
                                            DEFAULT_ITEM_TEXT,
                                            orders.getPrice() + "원"
                                    ),
                                    new Item(
                                            DEFAULT_ITEM_COUNT_TEXT,
                                            orders.getQuantity() + "개"
                                    ),
                                    new Item(
                                            DEFAULT_POINT_TEXT,
                                            orders.getPoint() + "포인트"
                                    ),
                            },
                            DEFAULT_SUM_TEXT,
                            orders.getTotalPrice() + "원"
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
