package gift.Controller;

import gift.DTO.RequestWishDTO;
import gift.Model.Entity.Member;
import gift.Model.Entity.Product;
import gift.Model.Entity.Wish;
import gift.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import gift.Service.WishService;
import gift.annotation.ValidUser;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member/wishes")
public class WishPageController {
    private final WishService wishService;
    private final ProductService productService;

    public WishPageController(WishService wishService, ProductService productService){
        this.wishService = wishService;
        this.productService = productService;
    }

    @GetMapping
    public String getWishlist (@ValidUser Member member, @PageableDefault(size=3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                               Model model){
        Page<Wish> wishlistPage = wishService.getWishList(member, pageable);
        model.addAttribute("wishes", wishlistPage.getContent());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", wishlistPage.getTotalPages());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sortField", pageable.getSort().iterator().next().getProperty());
        model.addAttribute("sortDir", pageable.getSort().iterator().next().getDirection().toString());
        return "wish-list";
    }

    @GetMapping("/new")
    public String showAddWishForm(Model model) {
        model.addAttribute("requestWishDTO", new RequestWishDTO());
        return "new-wish";
    }

    @PostMapping("/new")
    public String addWish(@ValidUser Member member, @ModelAttribute RequestWishDTO requestWishDTO) {
        wishService.addWish(member, requestWishDTO);
        return "redirect:/member/wishes";
    }

    @GetMapping("/edit/{productId}")
    public String showEditWishForm(@PathVariable("productId") Long productId, @ValidUser Member member, Model model) {
        Product product = productService.selectProduct(productId);
        Wish wish = wishService.findWishByMemberAndProduct(member, product);
        RequestWishDTO requestWishDTO = new RequestWishDTO(wish.getProduct().getId(), wish.getCount().getValue());
        model.addAttribute("requestWishDTO", requestWishDTO);
        return "edit-wish";
    }

    @PostMapping("/edit")
    public String editWish(@ValidUser Member member, @ModelAttribute RequestWishDTO requestWishDTO) {
        wishService.editWish(member, requestWishDTO);
        return "redirect:/member/wishes";
    }

    @PostMapping("/delete")
    public String deleteWish(@ValidUser Member member, @Valid @RequestBody RequestWishDTO requestWishDTO){
        wishService.deleteWish(member,requestWishDTO);
        return "redirect:/member/wishes";
    }


}
