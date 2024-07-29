package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.model.Member;
import gift.model.Product;
import gift.model.ProductOption;
import gift.model.Wish;
import gift.service.ProductOptionService;
import gift.service.ProductService;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/wishes")
@Tag(name = "Wish API", description = "APIs related to wish operations")
public class WishController {

    @Autowired
    private WishService wishService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductOptionService productOptionService;

    @Operation(summary = "위시 리스트 조회", description = "로그인한 사용자의 위시 리스트를 페이징하여 조회한다.")
    @GetMapping
    public Page<WishResponse> getWishes(@LoginMember Member member, Pageable pageable) {
        return wishService.getWishesByMemberId(member.getId(), pageable);
    }

    @Operation(summary = "위시 추가", description = "로그인한 사용자의 위시 리스트에 상품을 추가한다.")
    @PostMapping
    public Wish addWish(@RequestBody WishRequest wishRequest, @LoginMember Member member) {
        Product product = productService.findById(wishRequest.getProductId());
        ProductOption productOption = productOptionService.findProductOptionById(wishRequest.getOptionId());
        if (productOption == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product option");
        }
        Wish wish = new Wish();
        wish.setMember(member);
        wish.setProduct(product);
        wish.setProductOption(productOption);
        return wishService.addWish(wish);
    }

    @Operation(summary = "위시 삭제", description = "로그인한 사용자의 위시 리스트에서 상품을 삭제한다.")
    @DeleteMapping("/{wishId}")
    public void deleteWish(@PathVariable Long wishId, @LoginMember Member member) {
        wishService.deleteWish(wishId);
    }
}