package gift.wishlist.api;

import gift.member.validator.LoginMember;
import gift.global.pagination.dto.PageResponse;
import gift.product.dto.ProductResponse;
import gift.wishlist.application.WishesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wishes/list")
public class WishesViewController {

    private final WishesService wishesService;

    public WishesViewController(WishesService wishesService) {
        this.wishesService = wishesService;
    }

    @GetMapping
    public String getWishListView(@LoginMember Long memberId,
                                  Model model,
                                  @PageableDefault(
                                          sort = "id",
                                          direction = Sort.Direction.DESC)
                                  Pageable pageable) {
        Page<ProductResponse> wishes = wishesService.getWishlistOfMember(memberId, pageable);

        model.addAttribute("wishlist", wishes.getContent());
        model.addAttribute("wishlistPageInfo", new PageResponse(wishes));
        return "wish-list";
    }

}
