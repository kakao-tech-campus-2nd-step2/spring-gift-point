package gift.model;

public record WishListResponse(long id, long productId) {
    public static WishListResponse convertToDTO(WishList wishList) {
        return new WishListResponse(wishList.getId(), wishList.getProduct().getId());
    }
}
