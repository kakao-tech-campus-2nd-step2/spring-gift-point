package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.ErrorResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wishlist", description = "위시리스트 관리 API")
public class WishController {

    private final WishService wishService;

    @Autowired
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시리스트 조회", description = "로그인한 사용자의 위시리스트를 페이지별로 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> getAllByMemberId(@LoginMember Long memberId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<WishResponseDto> wishlist = wishService.getWishlist(memberId, pageable);

        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @Operation(summary = "위시리스트에 상품 추가", description = "로그인한 사용자의 위시리스트에 상품을 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "위시리스트 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "이미 위시리스트에 존재하는 상품", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<String> addWishlist(@LoginMember Long memberId,
        @RequestBody @Valid WishRequestDto wishRequestDto) {
        wishService.addWishlist(memberId, wishRequestDto.getProductId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "위시리스트에서 상품 삭제", description = "로그인한 사용자의 위시리스트에서 상품을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "이미 위시리스트에 존재하지 않는 상품", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping
    public ResponseEntity<String> deleteWishlist(@LoginMember Long memberId,
        @RequestBody Long productId) {
        wishService.deleteById(memberId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}