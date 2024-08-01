package gift.Controller;

import gift.DTO.WishDTO;
import gift.Service.WishService;
import gift.Mapper.WishServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishes")
public class WishController {

    @Autowired
    private WishService wishService;

    @Autowired
    private WishServiceMapper wishServiceMapper;

    @GetMapping
    public List<WishDTO> getAllWishes() {
        return wishService.findAllWishes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishDTO> getWishById(@PathVariable Long id) {
        return wishServiceMapper.toResponseEntity(wishService.findWishById(id));
    }

    @PostMapping
    public ResponseEntity<WishDTO> createWish(@RequestBody WishDTO wishDTO) {
        WishDTO savedWish = wishService.saveWish(wishDTO);
        return ResponseEntity.ok(savedWish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishDTO> updateWish(@PathVariable Long id, @RequestBody WishDTO wishDTO) {
        try {
            WishDTO updatedWish = wishService.updateWish(id, wishDTO);
            return ResponseEntity.ok(updatedWish);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long id) {
        wishService.deleteWish(id);
        return ResponseEntity.noContent().build();
    }
}
