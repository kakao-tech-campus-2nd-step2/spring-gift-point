package gift.model;

public record WishListDTO(long memberId, long productId, long quantity) {
    public WishListDTO withMemberId(long memberId) {
        return new WishListDTO(memberId, this.productId, this.quantity);
    }
}
