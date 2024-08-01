package gift.controller;

import gift.domain.Member;
import gift.dto.CommonResponse;
import gift.dto.WishAddResponseDto;
import gift.dto.WishPatchDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.JwtProvider;
import gift.service.MemberService;
import gift.service.WishService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wishes")
public class WishController {

    private final JwtProvider jwtProvider;
    private final WishService wishService;
    private final MemberService memberService;

    public WishController(JwtProvider jwtProvider, WishService wishService,MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.wishService = wishService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse> addWish(@RequestHeader("Authorization") String fullToken,
        @RequestBody WishRequestDto wishRequestDto) {
        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        WishResponseDto wishResponseDto = wishService.save(userEmail, wishRequestDto);
        WishAddResponseDto wishAddResponseDto = new WishAddResponseDto(wishResponseDto.getId(),wishResponseDto.getProductId());
        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResponse(wishAddResponseDto, "위시 생성 성공", true));
    }

//    @GetMapping
//    public ResponseEntity<Page<WishResponseDto>> findWishesByMemberId(
//        @RequestHeader("Authorization") String fullToken,
//        @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
//        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
//        return ResponseEntity.status(HttpStatus.OK).body(wishService.findByEmailPage(userEmail,pageable));
//    }

//    @DeleteMapping("/{productId}")
//    public ResponseEntity<Void> deleteWish(@RequestHeader("Authorization") String fullToken, @PathVariable Long productId){
//        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
//        wishService.deleteByUser(userEmail);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @GetMapping
    public ResponseEntity<CommonResponse> getMemberWishes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdDate,desc") String sort,
        @RequestHeader("Authorization") String fullToken) {

        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        Member member = memberService.findByEmail(userEmail);

        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResponse(
                wishService.getPagedMemberWishesByMemberId(member.getId(), pageable),
                "회원 위시 목록 조회 성공", true));
    }


    @DeleteMapping("/{wishId}")
    public ResponseEntity<CommonResponse> deleteWishById(
        @RequestHeader("Authorization") String fullToken, @PathVariable Long wishId) {
        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        wishService.deleteById(wishId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(new CommonResponse<>(null, "wish 삭제 성공", true));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Void> updateWish(@RequestHeader("Authorization") String fullToken,
        @PathVariable Long productId, @RequestBody
    WishPatchDto wishPatchDto) {
        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        wishService.updateWish(userEmail, productId, wishPatchDto.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
