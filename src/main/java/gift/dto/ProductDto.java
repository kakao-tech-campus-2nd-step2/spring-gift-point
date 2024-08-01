package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDto {

  private Long id;

  @Size(max = 15, message = "상품 이름은 최대 15자까지 가능합니다.")
  @NotBlank(message = "상품 이름은 필수 항목입니다.")
  private String name;

  @NotNull(message = "가격은 필수 항목입니다.")
  private int price;

  @NotBlank(message = "이미지 URL은 필수 항목입니다.")
  private String imageUrl;

  @NotNull(message = "카테고리 ID는 필수 항목입니다.")
  private Long categoryId;

  public ProductDto() {}

  public ProductDto(Long id, String name, int price, String imageUrl, Long categoryId) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.categoryId = categoryId;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public Long getCategoryId() {
    return categoryId;
  }
}
