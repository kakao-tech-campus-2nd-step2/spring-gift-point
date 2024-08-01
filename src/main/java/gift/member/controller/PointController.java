package gift.member.controller;

import gift.auth.domain.AuthInfo;
import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorResponseDto;
import gift.global.response.ResultCode;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import gift.global.security.Login;
import gift.global.utils.ResponseHelper;
import gift.member.domain.Member;
import gift.member.dto.MemberRequestDto;
import gift.member.dto.PointResponseDto;
import gift.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
