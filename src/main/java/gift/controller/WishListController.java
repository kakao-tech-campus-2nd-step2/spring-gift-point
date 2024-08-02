package gift.controller;

import gift.DTO.MemberDTO;
import gift.DTO.ProductDTO;
import gift.auth.LoginMember;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * WishListController 클래스는 위시리스트에 대한 RESTful API를 제공함
 */
@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "위시리스트 API", description = "위시리스트의 생성, 조회, 수정, 삭제를 수행하는 API입니다.")
public class WishListController {

    private final WishListService wishListService;

    @Autowired
    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    /**
     * 새로운 위시리스트 항목을 생성함
     *
     * @param productId 생성할 위시리스트 항목의 상품 ID
     * @param memberDTO 로그인된 사용자 정보
     * @return 생성된 위시리스트 항목의 정보
     */
    @Operation(summary = "위시리스트 항목 생성", description = "새로운 위시리스트 항목을 생성합니다.")
    @PostMapping
    public ResponseEntity<ProductDTO> createWishList(@RequestParam Long productId,
                                                     @LoginMember MemberDTO memberDTO) {
        ProductDTO newWishListIds = wishListService.addWishList(productId, memberDTO);
        return ResponseEntity.ok(newWishListIds);
    }

    /**
     * 로그인된 사용자의 모든 위시리스트 항목을 조회함
     *
     * @param memberDTO 로그인된 사용자 정보
     * @return 지정된 사용자의 모든 위시리스트 항목의 정보
     */
    @Operation(summary = "사용자 위시리스트 조회", description = "로그인된 사용자의 모든 위시리스트 항목을 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getWishListsByUserId(@LoginMember MemberDTO memberDTO) {
        List<ProductDTO> productIds = wishListService.getWishListsByUserId(memberDTO.getId());
        return ResponseEntity.ok(productIds);
    }

    /**
     * 사용자 ID를 통해 사용자의 위시리스트를 가져옵니다.
     *
     * @param memberDTO 회원 정보를 포함하는 DTO
     * @param page      페이지 번호, 기본값은 0
     * @param size      페이지 크기, 기본값은 10
     * @param criteria  정렬 기준, 기본값은 createdAt
     * @param direction 정렬 방향, 기본값은 desc
     * @return ProductDTO 목록을 포함한 ResponseEntity
     */
    @Operation(summary = "페이지네이션된 위시리스트 조회", description = "페이지네이션과 정렬을 사용하여 위시리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getWishListsByUserId(@LoginMember MemberDTO memberDTO,
                                                                 @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                                                 @RequestParam(required = false, defaultValue = "10", value = "size") int size,
                                                                 @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria,
                                                                 @RequestParam(required = false, defaultValue = "desc", value = "direction") String direction) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), criteria));
        List<ProductDTO> productIds = wishListService.getWishListsByUserId(memberDTO.getId(), pageable);
        return ResponseEntity.ok(productIds);
    }

    /**
     * 로그인된 사용자의 모든 위시리스트 항목을 삭제함
     *
     * @param memberDTO 로그인된 사용자 정보
     * @return 204 No Content
     */
    @Operation(summary = "모든 위시리스트 항목 삭제", description = "로그인된 사용자의 모든 위시리스트 항목을 삭제합니다.")
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteWishListsByUserId(@LoginMember MemberDTO memberDTO) {
        if (wishListService.deleteWishListsByUserId(memberDTO.getId())) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 로그인된 사용자가 지정된 상품을 위시리스트에서 삭제함
     *
     * @param productId 삭제할 상품의 ID
     * @param memberDTO 로그인된 사용자 정보
     * @return 204 No Content
     */
    @Operation(summary = "특정 위시리스트 항목 삭제", description = "로그인된 사용자가 지정된 상품을 위시리스트에서 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWishListByUserIdAndProductId(@PathVariable Long productId,
                                                                   @LoginMember MemberDTO memberDTO) {
        wishListService.deleteWishListByUserIdAndProductId(productId, memberDTO.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * 로그인된 사용자가 지정된 상품을 위시리스트에 추가함
     *
     * @param productId 추가할 상품의 ID
     * @param memberDTO 로그인된 사용자 정보
     * @return 200 OK
     */
    @Operation(summary = "특정 위시리스트 항목 추가", description = "로그인된 사용자가 지정된 상품을 위시리스트에 추가합니다.")
    @PostMapping("/{productId}")
    public ResponseEntity<Void> addWishListByUserIdAndProductId(@PathVariable Long productId,
                                                                @LoginMember MemberDTO memberDTO) {
        wishListService.addWishList(productId, memberDTO);
        return ResponseEntity.ok().build();
    }
}
