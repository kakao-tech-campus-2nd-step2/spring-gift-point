package gift.DTO;

public class WishListDto {

  private Long id;
  private MemberDto memberDto;
  private ProductDto productDto;

  public WishListDto() {
  }

  public WishListDto(Long id, MemberDto memberDto, ProductDto productDto) {
    this.id = id;
    this.memberDto = memberDto;
    this.productDto = productDto;
  }

  public WishListDto(MemberDto memberDto, ProductDto productDto) {
    this.memberDto = memberDto;
    this.productDto = productDto;
  }

  public Long getId() {
    return this.id;
  }

  public MemberDto getMemberDto() {
    return this.memberDto;
  }

  public ProductDto getProductDto() {
    return this.productDto;
  }
}
