package gift.dto;

public class WishDto {

    private final Long id;
    private final MemberDto memberDto;
    private final ProductDto productDto;

    public WishDto(Long id, MemberDto memberDto, ProductDto productDto) {
        this.id = id;
        this.memberDto = memberDto;
        this.productDto = productDto;
    }

    public Long getId() {
        return id;
    }

    public MemberDto getMemberDto() {
        return memberDto;
    }

    public ProductDto getProductDto() {
        return productDto;
    }
}
