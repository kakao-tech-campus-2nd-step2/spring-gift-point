package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.WishlistRequestDto;
import gift.dto.WishlistResponseDto;
import gift.entity.Wish;
import gift.service.MemberService;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WishList API")
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishService wishService;
    private final MemberService memberService;

    public WishlistController(WishService wishService, MemberService memberService) {
        this.wishService = wishService;
        this.memberService = memberService;
    }


    @Operation(summary = "모든 위시리스트 조회", description = "모든 위시리스트를 조회합니다.",
        parameters = {
            @Parameter(name = "page", description = "페이지 번호"),
            @Parameter(name = "size", description = "한페이지 크기")
        })
    @ApiResponses(
        value = {
            @ApiResponse(
            responseCode = "200",
            description = "위시리스트를 조회합니다.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = WishlistResponseDto.class))))
    })
    @GetMapping
    public ResponseEntity<List<WishlistResponseDto>> getAllWishlists(
        @LoginUser String email,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Long memberId = memberService.getMemberId(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<Wish> wishPage = wishService.findByMemberId(memberId, pageable);
        List<WishlistResponseDto> wishList = wishPage.stream()
            .map(wish -> new WishlistResponseDto(
                wish.getMember().getId(),
                wish.getProduct().getId()
            ))
            .collect(Collectors.toList());
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }


    @Operation(summary = "위시리스트 추가", description = "위시리스트를 추가합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "위시리스트 추가성공.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = WishlistResponseDto.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "위시리스트 추가실패, 잘못된상품"
            )
        }
    )
    @PostMapping
    public ResponseEntity<WishlistResponseDto> addWishlist (@LoginUser String email, @RequestBody WishlistRequestDto requestDto) {
        Long memberId = memberService.getMemberId(email);
        WishlistResponseDto wishlistResponseDto = new WishlistResponseDto(memberId, requestDto.getProductId());
        if(wishService.addWishlist(memberId, requestDto.getProductId())) {
            return new ResponseEntity<>(wishlistResponseDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(wishlistResponseDto, HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "위시리스트 삭제", description = "위시리스트를합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "위시리스트 삭제 완료",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 위시리스트 입니다."
            )
        }
    )
    @DeleteMapping("/{productId}")
    public ResponseEntity<Long> deleteWishlist(@PathVariable Long productId, @LoginUser String email) {
        Long memberId = memberService.getMemberId(email);
        if(wishService.deleteWishlist(memberId, productId)){
            return new ResponseEntity<>(memberId, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
