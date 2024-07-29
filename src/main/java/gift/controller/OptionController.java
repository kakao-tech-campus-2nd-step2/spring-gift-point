package gift.controller;

import gift.dto.giftorder.GiftOrderRequest;
import gift.dto.option.OptionAddRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import gift.service.KakaoService;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/options")
@Tag(name = "OPTION")
public class OptionController {

    private final OptionService optionService;

    private final KakaoService kakaoService;

    public OptionController(OptionService optionService, KakaoService kakaoService) {
        this.optionService = optionService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addOption(@Valid @RequestBody OptionAddRequest optionAddRequest) {
        var option = optionService.addOption(optionAddRequest);
        return ResponseEntity.created(URI.create("/api/options/" + option.id())).build();
    }

    @PostMapping("/order")
    public ResponseEntity<Void> orderOption(@RequestAttribute("memberId") Long memberId, @Valid @RequestBody GiftOrderRequest giftOrderRequest) {
        var order = optionService.orderOption(memberId, giftOrderRequest);
        kakaoService.sendSelfMessageOrder(memberId, order);
        return ResponseEntity.created(URI.create("/api/giftOrders/" + order.id())).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateOption(@PathVariable Long id, @Valid @RequestBody OptionUpdateRequest optionUpdateRequest) {
        optionService.updateOption(id, optionUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OptionResponse>> getOptions(@RequestParam Long productId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var options = optionService.getOptions(productId, pageable);
        return ResponseEntity.ok(options);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.noContent().build();
    }
}
