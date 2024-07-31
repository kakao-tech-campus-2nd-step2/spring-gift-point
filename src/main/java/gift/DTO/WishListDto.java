package gift.DTO;

public class WishListDto {

  private Long id;
  private ProductDto productDto;

  public WishListDto() {
  }

  public WishListDto(Long id, ProductDto productDto) {
    this.id = id;
    this.productDto = productDto;
  }

  public WishListDto(ProductDto productDto) {
    this.productDto = productDto;
  }

  public Long getId() {
    return this.id;
  }

  public ProductDto getProductDto() {
    return this.productDto;
  }
}
