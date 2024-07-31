package gift.controller;

import gift.auth.CheckRole;
import gift.auth.LoginMember;
import gift.paging.PagingService;
import gift.request.LoginMemberDto;
import gift.response.ProductListResponse;
import gift.response.ProductResponse;
import gift.request.WishListRequest;
import gift.exception.InputException;
import gift.model.Product;
import gift.service.WishService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WishListApiController {

    private final WishService wishService;
    private final PagingService pagingService;

    public WishListApiController(WishService wishService, PagingService pagingService) {
        this.wishService = wishService;
        this.pagingService = pagingService;
    }

    @CheckRole("ROLE_USER")
    @GetMapping("/api/wishes")
    public ResponseEntity<ProductListResponse> getWishList(
        @RequestParam(defaultValue = "1", name = "page") int page,
        @RequestParam(defaultValue = "10", name = "size") int size,
        @LoginMember LoginMemberDto memberDto) {
        PageRequest pageRequest = pagingService.makeWishPageRequest(page, size);
        Page<Product> pagedWishList = wishService.getPagedWishList(memberDto.id(),
            pageRequest);
        List<ProductResponse> productResponses = pagedWishList.getContent()
            .stream()
            .map(ProductResponse::createProductResponse)
            .toList();
        ProductListResponse dto = new ProductListResponse(productResponses,
            pagedWishList.getNumber(),
            pagedWishList.getSize(), pagedWishList.getTotalElements(),
            pagedWishList.getTotalPages());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @CheckRole("ROLE_USER")
    @PostMapping("/api/wishes")
    public ResponseEntity<Void> addWishList(@LoginMember LoginMemberDto memberDto,
        @RequestBody @Valid WishListRequest dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        wishService.addMyWish(memberDto.id(), dto.productId(), dto.quantity());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @CheckRole("ROLE_USER")
    @DeleteMapping("/api/wishes/{wishId}")
    public ResponseEntity<Void> deleteWishList(@PathVariable("wishId") Long id) {
        wishService.deleteMyWish(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
