package gift.controller.product;

import gift.application.product.ProductFacade;
import gift.application.wish.WishService;
import gift.controller.product.dto.OptionRequest;
import gift.controller.product.dto.OptionResponse;
import gift.application.product.service.OptionService;
import gift.application.product.dto.OptionModel;
import gift.global.auth.Authenticate;
import gift.global.auth.Authorization;
import gift.global.auth.LoginInfo;
import gift.model.member.Role;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionController {

    private final OptionService optionService;
    private final ProductFacade productFacade;
    private final WishService wishService;

    public OptionController(OptionService optionService, ProductFacade productFacade,
        WishService wishService) {
        this.optionService = optionService;
        this.productFacade = productFacade;
        this.wishService = wishService;
    }

    @Operation(summary = "옵션 목록 조회", description = "옵션 목록 조회 api")
    @GetMapping("/products/{id}/options")
    public ResponseEntity<OptionResponse.InfoList> getOptions(
        @PathVariable("id") Long productId
    ) {
        List<OptionModel.Info> models = optionService.getOptions(productId);
        OptionResponse.InfoList response = OptionResponse.InfoList.from(models);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "옵션 생성", description = "옵션 생성 api")
    @Authorization(role = Role.ADMIN)
    @PostMapping("/products/{id}/options")
    public ResponseEntity<OptionResponse.InfoList> createOption(
        @PathVariable("id") Long productId,
        @RequestBody @Valid OptionRequest.Register request
    ) {
        List<OptionModel.Info> models = optionService.createOption(productId, request.toCommand());
        OptionResponse.InfoList response = OptionResponse.InfoList.from(models);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "옵션 수정", description = "옵션 수정 api")
    @Authorization(role = Role.ADMIN)
    @PutMapping("/products/{productId}/options/{optionId}")
    public ResponseEntity<OptionResponse.Info> updateOption(
        @PathVariable("optionId") Long optionId,
        @PathVariable("productId") Long productId,
        @RequestBody @Valid OptionRequest.Update request
    ) {
        OptionModel.Info model = optionService.updateOption(optionId, productId,
            request.toCommand());
        OptionResponse.Info response = OptionResponse.Info.from(model);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "옵션 삭제", description = "옵션 삭제 api")
    @Authorization(role = Role.ADMIN)
    @DeleteMapping("/products/{productId}/options/{optionId}")
    public ResponseEntity<String> deleteOption(
        @PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId
    ) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.ok("Deleted correctly");
    }

    @Operation(summary = "옵션 구매", description = "옵션 구매 api")
    @Authorization(role = Role.USER)
    @PostMapping("/products/options/purchase")
    public ResponseEntity<String> purchaseOption(
        @RequestBody @Valid OptionRequest.Purchase request,
        @Authenticate LoginInfo loginInfo
    ) {
        OptionModel.Info optionInfo = productFacade.purchase(loginInfo.memberId(),
            request.toCommand());
        wishService.deleteWishByOption(loginInfo.memberId(), optionInfo.id());
        return ResponseEntity.ok("구매에 성공하셨습니다");
    }
}
