package gift.controller.member;

import gift.application.member.dto.MemberModel;
import gift.application.member.service.MemberService;
import gift.controller.member.dto.MemberRequest;
import gift.controller.member.dto.MemberResponse;
import gift.controller.member.dto.MemberResponse.Info;
import gift.global.auth.Authorization;
import gift.global.dto.PageResponse;
import gift.model.member.Role;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/members")
public class MemberAdminController {

    private final MemberService memberService;

    public MemberAdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "모든 회원 정보 조회", description = "모든 회원 정보 조회 api")
    @Authorization(role = Role.ADMIN)
    @GetMapping("")
    public ResponseEntity<PageResponse<MemberResponse.Info>> getAllUser(
        @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable
    ) {
        Page<MemberModel.Info> page = memberService.getMemberPaging(pageable);
        return ResponseEntity.ok(PageResponse.from(page, MemberResponse.Info::from));
    }

    @Operation(summary = "회원 포인트 충전", description = "회원 포인트 충전 api")
    @Authorization(role = Role.ADMIN)
    @PatchMapping("{memberId}/point")
    public ResponseEntity<MemberResponse.Point> depositPoint(
        @PathVariable("memberId") Long memberId,
        @RequestBody @Valid MemberRequest.DepositPoint request
    ) {
        Integer memberPoint = memberService.depositPoint(memberId, request.depositPoint());
        return ResponseEntity.ok(new MemberResponse.Point(memberPoint));
    }


}
