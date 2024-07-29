package gift.dto;

public class WishResponse {

    private Long id;
    private String productName;
    private Long memberId;

    public WishResponse(Long id, String productName, Long memberId) {
        this.id = id;
        this.productName = productName;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


}