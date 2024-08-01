package gift.dto.wishDto;

public class WishDto {
    private Long productId;

    public WishDto(Long productId){
        this.productId = productId;
    }

    public Long getProductId(){
        return productId;
    }
}
