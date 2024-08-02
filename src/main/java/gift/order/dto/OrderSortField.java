package gift.order.dto;

import gift.product.dto.ProductSortField;

public enum OrderSortField {
  ID("id"),
  NAME("name"),
  ORDER_DATE_TIME("orderDateTime");

  private final String fieldName;

  OrderSortField(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public static ProductSortField fromString(String value) {
    for (ProductSortField sortBy : ProductSortField.values()) {
      if (sortBy.getFieldName().equalsIgnoreCase(value)) {
        return sortBy;
      }
    }
    throw new IllegalArgumentException("유효하지 않은 정렬 필드입니다: " + value);
  }
}


