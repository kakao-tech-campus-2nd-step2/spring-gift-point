package gift.controller.restcontroller;

import gift.common.annotation.LoginMember;
import gift.controller.dto.response.PointResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Point", description = "Point Rest API")
@RestController
@RequestMapping("/api/members/point")
public class PointRestController {

    private final MemberService memberService;

    public PointRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    @Operation(summary = "내 포인트 조회", description = "내 포인트를 조회합니다.")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<PointResponse> getPoint(@NonNull @LoginMember Long memberId) {
        return ResponseEntity.ok().body(memberService.findPointById(memberId));
    }
}
