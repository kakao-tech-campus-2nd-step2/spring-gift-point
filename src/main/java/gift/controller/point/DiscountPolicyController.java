package gift.controller.point;

import gift.domain.point.DiscountPolicy;
import gift.domain.point.DiscountPolicy.CreateDiscountPolicy;
import gift.domain.point.DiscountPolicy.DiscountPolicyDetail;
import gift.domain.point.DiscountPolicy.DiscountPolicySimple;
import gift.service.point.DiscountPolicyService;
import gift.util.page.PageMapper;
import gift.util.page.PageResult;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "할인정책 관련 서비스")
@RestController
@RequestMapping("/api/discountPolicy")
public class DiscountPolicyController {

    private final DiscountPolicyService discountPolicyService;

    @Autowired
    public DiscountPolicyController(DiscountPolicyService discountPolicyService) {
        this.discountPolicyService = discountPolicyService;
    }

    @Operation(summary = "할인정책 리스트 조회")
    @GetMapping
    public PageResult<DiscountPolicySimple> getDiscountPolicyList(@Valid
        DiscountPolicy.getList param) {
        return PageMapper.toPageResult(discountPolicyService.getDiscountPolicyList(param));
    }

    @Operation(summary = "할인 정책 조회")
    @GetMapping("/{id}")
    public SingleResult<DiscountPolicyDetail> getDiscountPolicy(@PathVariable long id) {
        return new SingleResult<>(discountPolicyService.getDiscountPolicy(id));
    }

    @Operation(summary = "할인 정책 생성")
    @PostMapping
    public SingleResult<Long> createDiscountPolicy(@Valid @RequestBody
        CreateDiscountPolicy create) {
        return new SingleResult<>(discountPolicyService.createDiscountPolicy(create));
    }

    @Operation(summary = "할인정책 삭제")
    @DeleteMapping("/{id}")
    public SingleResult<Long> deleteDiscountPolicy(@PathVariable long id) {
        return new SingleResult<>(discountPolicyService.deleteDiscountPolicy(id));
    }
}
