package gift.dto.response;

import gift.domain.WishlistItem;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.stream.Collectors;

public class WishlistPageResponse {
    private List<WishlistItemResponse> content;
    private int number;
    private long totalElements;
    private int size;
    private boolean last;

    public WishlistPageResponse(List<WishlistItemResponse> content, int number, long totalElements, int size, boolean last) {
        this.content = content;
        this.number = number;
        this.totalElements = totalElements;
        this.size = size;
        this.last = last;
    }

    public static WishlistPageResponse fromWishlistPage(Page<WishlistItem> wishlistPage) {
        List<WishlistItemResponse> content = wishlistPage.getContent().stream()
                .map(WishlistItemResponse::fromWishlistItem)
                .collect(Collectors.toList());

        return new WishlistPageResponse(
                content,
                wishlistPage.getNumber(),
                wishlistPage.getTotalElements(),
                wishlistPage.getSize(),
                wishlistPage.isLast()
        );
    }

}
