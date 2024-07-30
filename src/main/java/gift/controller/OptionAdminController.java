package gift.controller;

import gift.controller.dto.KakaoApiDTO;
import gift.controller.dto.OptionRequest;
import gift.controller.dto.OptionResponse;
import gift.service.GiftService;
import gift.service.OptionService;
import gift.utils.JwtTokenProvider;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
public class OptionAdminController {
    OptionService optionService;
    GiftService giftService;
    JwtTokenProvider jwtTokenProvider;

    public OptionAdminController(OptionService optionService, GiftService giftService,
        JwtTokenProvider jwtTokenProvider) {
        this.optionService = optionService;
        this.giftService = giftService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionResponse> createOption(@PathVariable("productId") Long productId,@Valid @RequestBody OptionRequest optionRequest){
        OptionResponse optionResponse = optionService.addOption(optionRequest,productId);
        return ResponseEntity.ok(optionResponse);
    }

    @PutMapping("{productId}/options/{optionId}")
    public ResponseEntity<OptionResponse> putOption(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId,@Valid @RequestBody OptionRequest optionRequest){
        OptionResponse optionResponse = optionService.changeOption(optionRequest, productId,
            optionId);
        return ResponseEntity.ok(optionResponse);

    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Long> delete(@Valid @RequestBody Long id, @RequestHeader("Authorization") String token){
        String jwttoken = token.substring(7);
        String emailFromToken = jwtTokenProvider.getEmailFromToken(jwttoken);
        Long deleteId = optionService.deleteOption(id,emailFromToken);
        return ResponseEntity.ok(deleteId);
    }
}
