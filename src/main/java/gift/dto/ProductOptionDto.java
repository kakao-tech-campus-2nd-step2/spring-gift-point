package gift.dto;

public class ProductOptionDto {
  private Long id;
  private String name;
  private String value;
  private Long productId;

  public ProductOptionDto(Long id, String name, String value, Long productId) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.productId = productId;
  }

  public ProductOptionDto() {}

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public Long getProductId() {
    return productId;
  }
}