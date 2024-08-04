package gift.controller.restcontroller;

import gift.controller.dto.request.PointUpdateRequest;
import gift.controller.dto.response.MemberResponse;
import gift.controller.dto.response.PagingResponse;
import gift.controller.dto.response.PointResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final MemberService memberService;

    public AdminRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("members")
    @Operation(summary = "전체 멤버 조회", description = "전체 멤버를 조회합니다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<PagingResponse<MemberResponse>> getAllMembers(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PagingResponse<MemberResponse> members = memberService.findAllMemberPaging(pageable);
        return ResponseEntity.ok().body(members);
    }

    @PatchMapping("members/{memberId}/point")
    @Operation(summary = "특정 멤버 포인트 주입", description = "특정 멤버에서 포인트를 주입합니다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<PointResponse> getMemberPoint(
            @PathVariable("memberId") @NotNull @Min(1) Long memberId,
            @Valid @RequestBody PointUpdateRequest request
    ) {
        PointResponse response = memberService.updatePointById(memberId, request);
        return ResponseEntity.ok().body(response);
    }
}
