package gift.option.controller;

import gift.option.dto.GiftOptionRequest;
import gift.option.dto.GiftOptionResponse;
import gift.option.service.GiftOptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/products/{productId}/options")
@RestController
public class GiftOptionController {

    private final GiftOptionService giftOptionService;

    public GiftOptionController(GiftOptionService giftOptionService) {
        this.giftOptionService = giftOptionService;
    }

    @GetMapping
    public ResponseEntity<List<GiftOptionResponse>> getGiftOptions(
        @PathVariable @NotNull Long productId) {
        return ResponseEntity.ok(giftOptionService.readAll(productId));
    }

    @PostMapping
    public ResponseEntity<GiftOptionResponse> add(@PathVariable @NotNull Long productId,
        @RequestBody @Valid GiftOptionRequest giftOptionRequest) {
        var body = giftOptionService.create(productId, giftOptionRequest);

        return ResponseEntity.ok(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GiftOptionResponse> update(@PathVariable @NotNull Long productId, @PathVariable Long id,
        @RequestBody @Valid GiftOptionRequest giftOptionRequest) {
        var body = giftOptionService.update(productId,id,giftOptionRequest);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long productId, @PathVariable Long id) {
        giftOptionService.delete(productId,id);

        return ResponseEntity.ok().build();
    }
}
