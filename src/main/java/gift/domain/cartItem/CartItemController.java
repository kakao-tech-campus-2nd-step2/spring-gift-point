package gift.domain.cartItem;

import gift.domain.member.dto.LoginInfo;
import gift.domain.cartItem.dto.CartItemDTO;
import gift.global.resolver.Login;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/members/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public String cartPage(
        Model model,
        @PageableDefault(page = 0, sort = "id_asc")
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "sort", defaultValue = "id_asc") String sort,
        @Login LoginInfo loginInfo) {
        int size = 10; // default
        Sort sortObj = getSortObject(sort);

        List<CartItemDTO> cartItemDTOS = cartItemService.getProductsInCartByMemberIdAndPageAndSort(
            loginInfo.getId(),
            page,
            size,
            sortObj
        );

        model.addAttribute("products", cartItemDTOS);
        return "cart";
    }

    private Sort getSortObject(String sort) {
        switch (sort) {
            case "price_asc":
                return Sort.by(Sort.Direction.ASC, "price");
            case "price_desc":
                return Sort.by(Sort.Direction.DESC, "price");
            case "name_asc":
                return Sort.by(Sort.Direction.ASC, "name");
            case "name_desc":
                return Sort.by(Sort.Direction.DESC, "name");
            default:
                return Sort.by(Sort.Direction.ASC, "id");
        }
    }
}

