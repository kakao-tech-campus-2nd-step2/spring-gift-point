package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.DomainResponse;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.model.HttpResult;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wish API", description = "APIs related to wish operations")
public class WishController {

    @Autowired
    private WishService wishService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductOptionService productOptionService;

    @Operation(summary = "위시 리스트 상품 조회(페이지네이션 적용)", description = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
    @GetMapping
    public DomainResponse getWishes(@LoginMember Member member, Pageable pageable) {
        Page<WishResponse> wishes = wishService.getWishesByMemberId(member.getId(), pageable);
        List<Map<String, Object>> wishList = wishes.stream()
                .map(wish -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", wish.getId());
                    map.put("productName", wish.getProductName());
                    map.put("productPrice", wish.getProductPrice());
                    map.put("productImageurl", wish.getProductImageurl());
                    map.put("productCategory", wish.getProductCategory());
                    map.put("optionName", wish.getOptionName());
                    return map;
                })
                .collect(Collectors.toList());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Wishes retrieved successfully");
        return new DomainResponse(httpResult, wishList, HttpStatus.OK);
    }

    @Operation(summary = "위시 리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
    @PostMapping
    public DomainResponse addWish(@RequestBody WishRequest wishRequest, @LoginMember Member member) {
        Product product = productService.findById(wishRequest.getProductId());
        ProductOption productOption = productOptionService.findProductOptionById(wishRequest.getOptionId());
        if (productOption == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product option");
        }
        Wish wish = new Wish();
        wish.setMember(member);
        wish.setProduct(product);
        wish.setProductOption(productOption);
        Wish addedWish = wishService.addWish(wish);
        Map<String, Object> wishMap = new HashMap<>();
        wishMap.put("id", addedWish.getId());
        wishMap.put("productName", addedWish.getProduct().getName());
        wishMap.put("productPrice", addedWish.getProduct().getPrice());
        wishMap.put("productImageurl", addedWish.getProduct().getImageurl());
        wishMap.put("productCategory", addedWish.getProduct().getCategory().getName());
        wishMap.put("optionName", addedWish.getProductOption().getName());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Wish added successfully");
        return new DomainResponse(httpResult, List.of(wishMap), HttpStatus.OK);
    }

    @Operation(summary = "위시 리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
    @DeleteMapping("/{wishId}")
    public DomainResponse deleteWish(@PathVariable Long wishId, @LoginMember Member member) {
        wishService.deleteWish(wishId);
        HttpResult httpResult = new HttpResult(HttpStatus.NO_CONTENT.value(), "Wish deleted successfully");
        return new DomainResponse(httpResult, null, HttpStatus.NO_CONTENT);
    }
}