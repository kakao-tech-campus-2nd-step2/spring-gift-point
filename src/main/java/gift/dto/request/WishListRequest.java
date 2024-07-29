package gift.dto.request;

public class WishListRequest {
    
    private long productId;

    public WishListRequest(long productId){
        this.productId = productId;
    }
    
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
}
