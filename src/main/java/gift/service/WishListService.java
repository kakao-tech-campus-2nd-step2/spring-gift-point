package gift.service;

import gift.dto.WishListResponse;

public interface WishListService {

    void addProduct(long memberId, long productId);

    void deleteProduct(long memberId, long productId);

    void updateProduct(long memberId, long productId, int productValue);

    WishListResponse getWishList(long memberId);

    WishListResponse getWishListPage(long memberId, int pageNumber, int pageSize);

}
