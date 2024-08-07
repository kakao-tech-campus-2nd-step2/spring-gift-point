package gift.controller;

import gift.domain.MemberDomain.PointRequest;
import gift.domain.MemberDomain.PointResponse;
import gift.domain.MemberDomain.RemainPointResponse;
import gift.service.JwtService;
import gift.service.MemberPointService;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/member/point")

public class MemberPointController {
    private MemberPointService memberPointService;
    private JwtService jwtService;

    public MemberPointController(MemberPointService memberPointService, JwtService jwtService) {
        this.memberPointService = memberPointService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<PointResponse> getMemberPoint(){
        String jwtId = jwtService.getMemberId();
        Integer point = memberPointService.getPoint(jwtId);
        return ResponseEntity.ok().body(new PointResponse(point));
    }

    @PostMapping
    public ResponseEntity<RemainPointResponse> useMemberPoint(
            @RequestBody PointRequest pointRequests
    ) throws IllegalAccessException {
        String jwtId = jwtService.getMemberId();
        Integer remainPoint = memberPointService.usePoint(jwtId, pointRequests.point());
        return ResponseEntity.ok().body(new RemainPointResponse(remainPoint));
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<Map<String, String>> handleException(JwtException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("PointError", "허용되지 않는 요청입니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
