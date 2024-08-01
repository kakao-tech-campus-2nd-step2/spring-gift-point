package gift.dto;

public class WishDto {
  private final Long id;
  private final Long productId;
  private final String productName;
  private final int productPrice;
  private final String productImageUrl;
  private final Long memberId;
  private final Long optionId;

  public WishDto(Long id, Long productId, String productName, int productPrice, String productImageUrl, Long memberId, Long optionId) {
    this.id = id;
    this.productId = productId;
    this.productName = productName;
    this.productPrice = productPrice;
    this.productImageUrl = productImageUrl;
    this.memberId = memberId;
    this.optionId = optionId;
  }

  public Long getId() {
    return id;
  }

  public Long getProductId() {
    return productId;
  }

  public String getProductName() {
    return productName;
  }

  public int getProductPrice() {
    return productPrice;
  }

  public String getProductImageUrl() {
    return productImageUrl;
  }

  public Long getMemberId() {
    return memberId;
  }

  public Long getOptionId() {
    return optionId;
  }
}