package gift.member.controller;

import gift.global.argumentresolver.LoginMember;
import gift.member.dto.PointDto;
import gift.member.entity.Member;
import gift.member.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Point", description = "포인트 관련 API")
@RestController
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @Operation(summary = "포인트 조회", description = "해당 회원의 포인트를 조회합니다.")
    @GetMapping
    public ResponseEntity<PointDto> getPoint(@LoginMember Member member) {
        return ResponseEntity.ok().body(pointService.getPoint(member));
    }
    
    @Operation(summary = "포인트 추가", description = "해당 회원의 포인트를 추가합니다.")
    @PostMapping
    public ResponseEntity<PointDto> addPoint(@LoginMember Member member, @RequestBody PointDto pointDto) {
        return ResponseEntity.ok().body(pointService.addPoint(member, pointDto.point()));
    }
}
