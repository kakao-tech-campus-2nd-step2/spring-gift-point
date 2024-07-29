package gift.controller;

import gift.service.ProductOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/products/options")
public class ProductOptionController {

    @Autowired
    private ProductOptionService productOptionService;

    @PostMapping("/{optionId}/subtract")
    public ResponseEntity<String> subtractOptionQuantity(@PathVariable Long optionId, @RequestBody Map<String, Integer> request) {
        if (request == null || !request.containsKey("quantity")) {
            return ResponseEntity.badRequest().body("Quantity is required");
        }

        int quantityToSubtract = request.get("quantity");
        try {
            productOptionService.subtractProductOptionQuantity(optionId, quantityToSubtract);
            return ResponseEntity.ok("Quantity 차감 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
