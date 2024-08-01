package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishRequest;
import gift.model.Member;
import gift.model.Wish;
import gift.service.WishService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getWishes(@LoginMember Member member) {
        List<Wish> wishes = wishService.getWishesByMember(member);
        Map<String, Object> response = new HashMap<>();
        response.put("wishes", wishes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addWish(@RequestBody WishRequest wishRequest,
        @LoginMember Member member) {
        Wish savedWish = wishService.addWish(wishRequest.getProductId(),member);
        Map<String, Object> response = new HashMap<>();
        response.put("wish", savedWish);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> removeWish(@PathVariable Long id,
        @LoginMember Member member) {
        boolean removed = wishService.removeWish(id, member);
        Map<String, Object> response = new HashMap<>();
        response.put("removed", removed);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getWishesById(@PathVariable Long productId,
        @LoginMember Member member) {
        List<Wish> wishes = wishService.getWishesByMember(member);
        Map<String, Object> response = new HashMap<>();
        response.put("wishes", wishes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
