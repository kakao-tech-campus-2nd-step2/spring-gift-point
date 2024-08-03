package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.pointDTO.PointRequestDTO;
import gift.exception.AuthorizationFailedException;
import gift.model.Member;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/points")
@Tag(name = "포인트 관리 API", description = "포인트 관리를 위한 API")
public class AdminPointController {

    private final MemberService memberService;

    public AdminPointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @Operation(summary = "포인트 충전", description = "관리자가 다른 사용자의 포인트를 충전시킵니다.")
    public ResponseEntity<Void> chargePoints(
        @Valid @RequestBody PointRequestDTO pointRequestDTO,
        @LoginMember Member member) {
        if (member == null || !"admin".equals(member.getRole())) {
            throw new AuthorizationFailedException("관리자 권한이 없습니다.");
        }
        memberService.chargePoints(pointRequestDTO);
        return ResponseEntity.ok().build();
    }

}
