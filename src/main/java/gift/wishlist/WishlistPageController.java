package gift.wishlist;

import gift.product.ProductService;
import gift.product.dto.ProductPaginationResponseDTO;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class WishlistPageController {

    private final WishlistService wishlistService;
    private final ProductService productService;

    public WishlistPageController(
        WishlistService wishlistService,
        ProductService productService
    ) {
        this.wishlistService = wishlistService;
        this.productService = productService;
    }

    @Deprecated
    @GetMapping("/wishlist")
    public String wishlist() {
        return "wishlist/emptyWishlistPage";
    }

    @Deprecated
    @GetMapping("/wishlistPage/{categoryId}")
    public String wishlistPage(
        @RequestHeader("Authorization") String token,
        Model model,
        Pageable pageable,
        @PathVariable long categoryId
    ) {
        pageable = changePageable(pageable);
        Page<ProductPaginationResponseDTO> wishProducts = wishlistService.getWishlists(token,
            pageable);
        Page<ProductPaginationResponseDTO> allProducts = productService.getAllProductsByCategoryId(
            PageRequest.of(0, Integer.MAX_VALUE),
            categoryId
        );
        wishProducts.getSort().getOrderFor("product.id");

        model.addAttribute("wishProducts", wishProducts);
        model.addAttribute("page", pageable.getPageNumber() + 1);
        model.addAttribute("totalProductsSize", wishProducts.getTotalElements());
        model.addAttribute("currentPageProductSize", wishProducts.get().toList().size());
        model.addAttribute("pageLists",
            IntStream.range(1, wishProducts.getTotalPages() + 1).boxed().toList());

        model.addAttribute("allProducts", allProducts);

        return "wishlist/page";
    }

    private Pageable changePageable(Pageable pageable) {
        List<Order> orders = pageable.getSort()
            .stream()
            .map(this::changeReferenceOfOrder)
            .toList();

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
    }

    private Order changeReferenceOfOrder(Order order) {
        return new Order(order.getDirection(), "product." + order.getProperty());
    }
}
