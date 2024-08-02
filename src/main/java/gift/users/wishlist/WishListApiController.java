package gift.users.wishlist;

import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import gift.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "wish API", description = "wishes related API")
public class WishListApiController {

    private final WishListService wishListService;

    public WishListApiController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    @Operation(summary = "get all user wishes", description = "회원의 모든 위시리스트를 조회합니다.")
    public ResponseEntity<ApiResponse<Page<WishListDTO>>> getWishList(
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sort", required = false, defaultValue = "id,asc") String sort,
        HttpServletRequest request) {

        size = PageUtil.validateSize(size);
        String[] sortParams = PageUtil.validateSort(sort,
            Arrays.asList("id", "productId", "num", "createdDate"));
        String sortBy = sortParams[0];
        Direction direction = PageUtil.validateDirection(sortParams[1]);

        Long userId = (Long) request.getAttribute("userId");
        Page<WishListDTO> wishLists = wishListService.getWishListsByUserId(userId, page, size,
            direction, sortBy);
        ApiResponse<Page<WishListDTO>> apiResponse = new ApiResponse<>(HttpResult.OK,
            "위시리스트 전체 조회 성공", HttpStatus.OK, wishLists);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    @Operation(summary = "add one wish", description = "회원의 위시리스트에 하나의 위시리스트를 추가합니다. required "
        + "info(productId, num, optionId)")
    public ResponseEntity<ApiResponse<WishListDTO>> addWishList(
        @Valid @RequestBody WishListDTO wishListDTO,
        HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        WishListDTO result = wishListService.addWishList(wishListDTO, userId);
        ApiResponse<WishListDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "위시리스트 추가 성공",
            HttpStatus.CREATED, result);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{wishId}")
    @Operation(summary = "update one wish", description = "위시리스트 아이디로 회원의 위시리스트를 수정합니다.")
    public ResponseEntity<ApiResponse<WishListDTO>> updateWishList(
        @PathVariable("wishId") Long wishId,
        @Valid @RequestBody WishListDTO wishListDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        WishListDTO result = wishListService.updateWishList(userId, wishId, wishListDTO);
        ApiResponse<WishListDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "위시리스트 수정 성공",
            HttpStatus.OK, result);
        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "delete one wish", description = "위시리스트 아이디로 하나의 위시리스트를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteWishListByWishListId(
        @PathVariable("wishId") Long wishId,
        HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        wishListService.deleteWishListByWishListId(userId, wishId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpResult.OK, "위시리스트 삭제 성공",
            HttpStatus.OK, null);
        return ResponseEntity.ok(apiResponse);
    }
}
