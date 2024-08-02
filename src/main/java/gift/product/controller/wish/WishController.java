package gift.product.controller.wish;

import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.wish.PageWishResponse;
import gift.product.dto.wish.WishDto;
import gift.product.dto.wish.WishResponse;
import gift.product.exception.ExceptionResponse;
import gift.product.model.Wish;
import gift.product.service.WishService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "wish", description = "위시 리스트 관련 API")
@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageWishResponse.class))),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<WishResponse>> getWishAll(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size,
        @RequestParam(name = "sort", defaultValue = "createdDate,asc") String sortParam,
        HttpServletRequest request) {

        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        String[] sortParamSplited = sortParam.split(",");
        String sort = sortParamSplited[0];
        String direction = (sortParamSplited.length > 1) ? sortParamSplited[1] : "asc";

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);

        return ResponseEntity.ok(wishService.getWishAll(pageable, loginMemberIdDto));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wish.class))),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Wish> getWish(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        Wish wish = wishService.getWish(id, loginMemberIdDto);
        return ResponseEntity.ok(wish);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "위시 리스트 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("{productId}")
    public ResponseEntity<Void> insertWish(@PathVariable(name = "productId") Long productId,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        wishService.insertWish(new WishDto(productId), loginMemberIdDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "위시 리스트 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(@PathVariable(name = "wishId") Long wishId,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        wishService.deleteWish(wishId, loginMemberIdDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private LoginMemberIdDto getLoginMember(HttpServletRequest request) {
        return new LoginMemberIdDto((Long) request.getAttribute("id"));
    }
}
