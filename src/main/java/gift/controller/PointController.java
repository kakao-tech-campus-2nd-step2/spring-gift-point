package gift.controller;

import gift.dto.PointDto;
import gift.service.MemberService;
import gift.vo.LoginMember;
import gift.vo.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
@Tag(name = "포인트 관리", description = "포인트 충전, 조회 관련된 API들을 제공합니다. ")
public class PointController {

    private final MemberService memberService;

    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping()
    @Operation(
            summary = "포인트 조회",
            description = "회원 포인트를 조회하는 API입니다."
    )
    @Parameter(name = "member", description = "회원 정보가 담긴 인증 헤더", required = true)
    public ResponseEntity<PointDto> getPoint(@LoginMember Member member) {
        return ResponseEntity.ok().body(new PointDto(member.getPoint()));
    }

    @PostMapping()
    @Operation(
            summary = "포인트 충전",
            description = "회원의 포인트를 충전하는 API입니다."
    )
    @Parameters({
            @Parameter(name = "pointDto", description = "포인트 정보가 담긴 DTO", required = true),
            @Parameter(name = "member", description = "회원 정보가 담긴 인증 헤더")
    })
    public ResponseEntity<Void> addPoint(@RequestBody @Valid PointDto pointDto, @LoginMember Member member) {
        memberService.updateMemberPoint(member.getId(), pointDto.point());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}