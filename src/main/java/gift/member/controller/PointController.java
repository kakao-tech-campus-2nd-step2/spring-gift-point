package gift.member.controller;

import gift.auth.domain.AuthInfo;
import gift.global.security.Login;
import gift.member.dto.PointResponseDto;
import gift.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final MemberService memberService;

    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public ResponseEntity<PointResponseDto> getPoint(@Login AuthInfo authInfo) {
        Long point = memberService.getPoint(authInfo.memberId());
        PointResponseDto pointResponseDto = new PointResponseDto(point);
        return ResponseEntity.status(200)
                .body(pointResponseDto);
    }
}
