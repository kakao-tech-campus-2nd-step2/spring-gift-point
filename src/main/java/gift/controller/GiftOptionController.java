package gift.controller;

import gift.dto.GiftOptionRequest;
import gift.dto.GiftOptionResponse;
import gift.service.GiftOptionService;
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
    public ResponseEntity<Void> add(@PathVariable @NotNull Long productId,
        @RequestBody @Valid GiftOptionRequest giftOptionRequest) {
        giftOptionService.create(productId, giftOptionRequest);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable @NotNull Long productId, @PathVariable Long id,
        @RequestBody @Valid GiftOptionRequest giftOptionRequest) {
        giftOptionService.update(productId,id,giftOptionRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long productId, @PathVariable Long id) {
        giftOptionService.delete(productId,id);

        return ResponseEntity.ok().build();
    }
}
