package gift.controller;

import gift.authentication.LoginMember;
import gift.authentication.UserDetails;
import gift.dto.PointResponseDto;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Point", description = "포인트 관련 API")
@RestController
@RequestMapping("/api/points")
public class PointController {
    private final MemberService memberService;

    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<PointResponseDto> getPoint(@LoginMember UserDetails userDetails) {
        return ResponseEntity.ok(memberService.getPoint(userDetails.id()));
    }
}
