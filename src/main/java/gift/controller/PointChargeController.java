package gift.controller;

import gift.domain.PointCharge;
import gift.domain.PointCharge.CreatePointCharge;
import gift.domain.PointCharge.PointChargeDetail;
import gift.domain.PointCharge.PointChargeSimple;
import gift.service.PointChargeService;
import gift.util.page.PageMapper;
import gift.util.page.PageResult;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포인트 충전 관련 서비스")
@RestController
@RequestMapping("/api/point/charge")
public class PointChargeController {

    private final PointChargeService pointChargeService;

    @Autowired
    public PointChargeController(PointChargeService pointChargeService) {
        this.pointChargeService = pointChargeService;
    }

    @Operation(summary = "포인트 충전 내역 리스트 조회")
    @GetMapping
    public PageResult<PointChargeSimple> getPointChargeList(HttpServletRequest req,
        @Valid PointCharge.getList param) {
        return PageMapper.toPageResult(pointChargeService.getPointChargeList(req, param));
    }

    @Operation(summary = "포인트 충전 내역 조회")
    @GetMapping("/{id}")
    public SingleResult<PointChargeDetail> getPointCharge(HttpServletRequest req,
        @PathVariable long id) {
        return new SingleResult<>(pointChargeService.getPointCharge(req, id));
    }

    @Operation(summary = "포인트 충전")
    @PostMapping
    public SingleResult<Long> createPointCharge(HttpServletRequest req, @Valid @RequestBody CreatePointCharge create) {
        return new SingleResult<>(pointChargeService.createPointCharge(req, create));
    }

    @Operation(summary = "포인트 충전 취소")
    @DeleteMapping("/{id}")
    public SingleResult<Long> revokePointCharge(HttpServletRequest req, @PathVariable long id) {
        return new SingleResult<>(pointChargeService.revokePointCharge(req, id));
    }
}
