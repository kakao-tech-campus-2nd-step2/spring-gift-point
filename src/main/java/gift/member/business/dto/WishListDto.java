package gift.member.business.dto;

import gift.member.persistence.entity.Wishlist;
import gift.product.persistence.entity.Product;
import java.util.List;

public record WishListDto(
    Long id,
    Long productId,
    String productName,
    Integer price,
    String imageUrl,
    Integer count
) {

    public static WishListDto of(Long id, Product product, Integer count) {
        return new WishListDto(id, product.getId(), product.getName(), product.getPrice(),
            product.getUrl(), count);
    }

    public static List<WishListDto> from(List<Wishlist> content) {
        return content.stream()
            .map(wishlist -> of(wishlist.getId(), wishlist.getProduct(), wishlist.getCount()))
            .toList();
    }
}
