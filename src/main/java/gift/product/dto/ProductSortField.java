package gift.product.dto;

public enum ProductSortField {
  ID("id"),
  NAME("name"),
  PRICE("price");

  private final String fieldName;

  ProductSortField(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public static ProductSortField fromString(String value) {
    for (ProductSortField sortBy : ProductSortField.values()) {
      if (sortBy.fieldName.equalsIgnoreCase(value)) {
        return sortBy;
      }
    }
    throw new IllegalArgumentException("유효하지 않은 정렬 필드입니다: " + value);
  }
}
