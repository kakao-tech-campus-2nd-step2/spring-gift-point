package gift.wishes.restapi;

import gift.advice.LoggedInUser;
import gift.core.PagedDto;
import gift.core.domain.product.Product;
import gift.core.domain.product.ProductService;
import gift.core.domain.wishes.WishesService;
import gift.wishes.restapi.dto.request.AddWishRequest;
import gift.wishes.restapi.dto.response.PagedWishResponse;
import gift.wishes.service.WishDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@Tag(name = "위시리스트")
public class WishesController {
    private final WishesService wishesService;
    private final ProductService productService;

    @Autowired
    public WishesController(
            WishesService wishesService,
            ProductService productService
    ) {
        this.wishesService = wishesService;
        this.productService = productService;
    }

    @GetMapping("/api/wishes")
    @Operation(
            summary = "위시리스트 조회",
            description = "위시리스트를 조회합니다.",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (기본 값 : 0)"),
                    @Parameter(name = "size", description = "페이지 크기 (기본 값 : 10)")
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "위시리스트를 조회합니다.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedWishResponse.class))
    )
    public PagedWishResponse getWishes(
            @LoggedInUser Long userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PagedDto<WishDto> pagedWishes = wishesService.getWishlistOfUser(userId, pageable);
        return PagedWishResponse.from(pagedWishes);
    }

    @PostMapping("/api/wishes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "위시리스트 추가", description = "위시리스트에 상품을 추가합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "위시리스트에 상품을 추가합니다."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "상품을 찾을 수 없습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "위시리스트에 상품 추가 실패"
                    )
            }
    )
    public void addWish(@LoggedInUser Long userId, @RequestBody AddWishRequest request) {
        Product product = productService.get(request.productId());
        wishesService.addProductToWishes(userId, product);
    }

    @DeleteMapping("/api/wishes/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "위시리스트 삭제",
            description = "위시리스트에서 상품을 삭제합니다.",
            parameters = {
                    @Parameter(name = "productId", description = "상품 ID")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "위시리스트에서 상품을 삭제합니다."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "위시리스트에서 상품을 찾을 수 없습니다."
                    )
            }
    )
    public void removeWish(@LoggedInUser Long userId, @PathVariable Long productId) {
        Product product = productService.get(productId);
        wishesService.removeProductFromWishes(userId, product);
    }
}