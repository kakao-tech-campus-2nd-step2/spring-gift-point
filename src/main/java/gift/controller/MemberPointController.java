package gift.controller;

import gift.domain.MemberDomain.MemberPoint;
import gift.domain.MemberDomain.PointRequest;
import gift.domain.MemberDomain.PointResponse;
import gift.domain.WishListDomain.WishListResponse;
import gift.service.JwtService;
import gift.service.MemberPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/member/point")

public class MemberPointController {
    private MemberPointService memberPointService;
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<PointResponse> getMemberPoint(
            @RequestBody PointRequest pointRequest
    ){
        String jwtId = jwtService.getMemberId();
        Integer point = memberPointService.getPoint(jwtId);
        return ResponseEntity.ok().body(new PointResponse(point));
    }
}
