package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.request.MemberRequest;
import gift.dto.request.WishRequest;
import gift.dto.response.CommonResponse;
import gift.dto.response.WishAddResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wishes")
public class WishRestController {
    private final WishService wishService;

    public WishRestController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "새로운 위시를 추가합니다")
    @PostMapping
    public ResponseEntity<CommonResponse> addWish(@LoginMember MemberRequest memberRequest, @RequestBody WishRequest wishRequest){
        WishAddResponse wishAddResponse = wishService.save(memberRequest, wishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse(wishAddResponse,"위시 생성 성공", true));
    }

    @Operation(summary = "특정 위시를 삭제합니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> removeWish(@LoginMember MemberRequest memberRequest, @PathVariable Long id){
        wishService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new CommonResponse<>(null, "위시 삭제 성공", true));
    }

    @Operation(summary = "회원의 모든 위시 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<CommonResponse> getMemberWishes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate,desc") String sort,
            @LoginMember MemberRequest memberRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse(wishService.getPagedMemberWishesByMemberId(memberRequest.id(),page,size,sort),"회원 위시 목록 조회 성공",true));
    }
}
