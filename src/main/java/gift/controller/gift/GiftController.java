package gift.controller.gift;

import gift.dto.gift.GiftRequest;
import gift.dto.gift.GiftResponse;
import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.service.gift.GiftService;
import gift.service.option.OptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class GiftController implements GiftSpecification {

    private final GiftService giftService;

    @Autowired
    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @PostMapping
    public ResponseEntity<String> addGift(@Valid @RequestBody GiftRequest.Create giftRequest) {
        giftService.addGift(giftRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 생성되었습니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftResponse> getGift(@PathVariable Long id) {
        GiftResponse gift = giftService.getGift(id);
        return ResponseEntity.ok(gift);
    }

    @GetMapping
    public ResponseEntity<PagingResponse<GiftResponse>> getAllGift(@ModelAttribute PagingRequest pagingRequest) {
        PagingResponse<GiftResponse> response = giftService.getAllGifts(pagingRequest.getPage(), pagingRequest.getSize());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGift(@PathVariable Long id, @RequestBody GiftRequest.Update giftRequest) {
        giftService.updateGift(giftRequest, id);
        return ResponseEntity.ok("상품 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGift(@PathVariable Long id) {
        giftService.deleteGift(id);
        return ResponseEntity.noContent().build();
    }
}