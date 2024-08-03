package gift.controller;


import gift.annotation.LoginMember;
import gift.dto.pointDTO.PointResponseDTO;
import gift.exception.AuthorizationFailedException;
import gift.model.Member;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@Tag(name = "포인트 조회 API", description = "포인트 조회를 위한 API")
public class PointController {

    private final MemberService memberService;

    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @Operation(summary = "포인트 조회", description = "멤버(사용자)의 포인트를 조회합니다.")
    public ResponseEntity<PointResponseDTO> getPoints(@LoginMember Member member) {
        if (member == null) {
            throw new AuthorizationFailedException("인증되지 않은 사용자입니다.");
        }
        PointResponseDTO pointResponseDTO = memberService.getPoints(member.getEmail());
        return ResponseEntity.ok(pointResponseDTO);
    }

}
