package gift.wish.dto;

import gift.product.dto.ProductSortField;

public enum WishSortField {
  ID("id"),
  createdAt("createdAt"),
  COUNT("count");

  private final String fieldName;

  WishSortField(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }

  public static WishSortField fromString(String value) {
    for (WishSortField sortBy : WishSortField.values()) {
      if (sortBy.fieldName.equalsIgnoreCase(value)) {
        return sortBy;
      }
    }
    throw new IllegalArgumentException("유효하지 않은 정렬 필드입니다: " + value);
  }
}
