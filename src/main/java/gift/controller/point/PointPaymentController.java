package gift.controller.point;

import gift.domain.point.PointPayment;
import gift.domain.point.PointPayment.CreatePointPayment;
import gift.domain.point.PointPayment.PointPaymentDetail;
import gift.domain.point.PointPayment.PointPaymentSimple;
import gift.service.point.PointPaymentService;
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

@Tag(name = "포인트 결제 관련 서비스")
@RestController
@RequestMapping("/api/point/payment")
public class PointPaymentController {

    private final PointPaymentService pointPaymentService;

    @Autowired
    public PointPaymentController(PointPaymentService pointPaymentService) {
        this.pointPaymentService = pointPaymentService;
    }

    @Operation(summary = "포인트 결제 내역 리스트 조회")
    @GetMapping
    public PageResult<PointPaymentSimple> getPointPaymentList(HttpServletRequest req,
        @Valid PointPayment.getList param) {
        return PageMapper.toPageResult(pointPaymentService.getPointPaymentList(req, param));
    }

    @Operation(summary = "포인트 결제 내역 조회")
    @GetMapping("/{id}")
    public SingleResult<PointPaymentDetail> getPointPayment(HttpServletRequest req,
        @PathVariable long id) {
        return new SingleResult<>(pointPaymentService.getPointPayment(req, id));
    }

    @Operation(summary = "포인트 결제")
    @PostMapping
    public SingleResult<Long> createPointPayment(HttpServletRequest req, @Valid @RequestBody
        CreatePointPayment create) {
        return new SingleResult<>(pointPaymentService.createPointPayment(req, create));
    }

    @Operation(summary = "포인트 결제 취소")
    @DeleteMapping("/{id}")
    public SingleResult<Long> revokePointPayment(HttpServletRequest req, @PathVariable long id) {
        return new SingleResult<>(pointPaymentService.revokePointPayment(req, id));
    }
}
