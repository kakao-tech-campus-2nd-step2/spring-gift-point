package gift.wishlist;

import gift.member.MemberTokenResolver;
import gift.product.Product;
import gift.product.ProductService;
import gift.token.MemberTokenDTO;
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

    @GetMapping("/wishlist")
    public String wishlist() {
        return "/wishlist/emptyWishlistPage";
    }

    @GetMapping("/wishlistPage")
    public String wishlistPage(
        @MemberTokenResolver MemberTokenDTO token,
        Model model,
        Pageable pageable
    ) {
        pageable = changePageable(pageable);
        Page<Product> wishProducts = wishlistService.getAllWishlists(token, pageable);
        List<Product> allProducts = productService.getAllProducts();
        wishProducts.getSort().getOrderFor("product.id");

        model.addAttribute("wishProducts", wishProducts);
        model.addAttribute("page", pageable.getPageNumber() + 1);
        model.addAttribute("totalProductsSize", wishProducts.getTotalElements());
        model.addAttribute("currentPageProductSize", wishProducts.get().toList().size());
        model.addAttribute("pageLists",
            IntStream.range(1, wishProducts.getTotalPages() + 1).boxed().toList());

        model.addAttribute("allProducts", allProducts);

        return "/wishlist/page";
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
