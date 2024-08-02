package gift.Controller;

import gift.DTO.WishDTO;
import gift.Service.WishService;
import gift.Mapper.WishServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/wish")
@Tag(name = "유저의 관심목록 관련 서비스", description = " ")
public class WishController {

    @Autowired
    private WishService wishService;

    @Autowired
    private WishServiceMapper wishServiceMapper;

    @Operation(summary = "유저 위시 리스트 조회", description = "위시리스트 id, 유저id, 상품id 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of wishes")
    })
    @GetMapping
    public List<WishDTO> getAllWishes() {
        return wishService.findAllWishes();
    }

    @Operation(summary = "유저 위시 조회", description = "위시리스트 id, 유저id, 상품id, 상품명, 상품가격, 상품 이미지 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved wish"),
            @ApiResponse(responseCode = "404", description = "Wish not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WishDTO> getWishById(@PathVariable Long id) {
        return wishServiceMapper.toResponseEntity(wishService.findWishById(id));
    }

    @Operation(summary = "유저 위시 추가", description = " ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created wish"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<WishDTO> createWish(@RequestBody WishDTO wishDTO) {
        WishDTO savedWish = wishService.saveWish(wishDTO);
        return ResponseEntity.ok(savedWish);
    }

    @Operation(summary = "유저 위시 수정", description = " ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated wish"),
            @ApiResponse(responseCode = "404", description = "Wish not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WishDTO> updateWish(@PathVariable Long id, @RequestBody WishDTO wishDTO) {
        try {
            WishDTO updatedWish = wishService.updateWish(id, wishDTO);
            return ResponseEntity.ok(updatedWish);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "유저 위시 삭제", description = " ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted wish"),
            @ApiResponse(responseCode = "404", description = "Wish not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long id) {
        wishService.deleteWish(id);
        return ResponseEntity.noContent().build();
    }
}
