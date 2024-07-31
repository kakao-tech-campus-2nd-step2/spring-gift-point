package gift.wishlist.dto;

import gift.product.dto.WishOptionResponse;

public record WishResponse(
        Long id,
        WishOptionResponse option
) { }
