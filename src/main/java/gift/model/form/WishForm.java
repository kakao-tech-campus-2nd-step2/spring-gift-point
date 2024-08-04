package gift.model.form;

public class WishForm {

    private Long productId;

    public WishForm(Long productId) {
        this.productId = productId;
    }

    public WishForm() {
    }

    public Long getProductId() {
        return productId;
    }
}
