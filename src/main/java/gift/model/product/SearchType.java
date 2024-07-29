package gift.model.product;

public enum SearchType {
    NAME("이름"),
    PRICE("가격"),
    CATEGORY("카테고리"),
    ALL("전체");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }

}
