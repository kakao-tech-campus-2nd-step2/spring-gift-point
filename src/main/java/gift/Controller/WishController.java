package gift.Controller;

import gift.DTO.RequestWishDTO;
import gift.DTO.ResponseOrderDTO;
import gift.DTO.ResponseWishDTO;
import gift.Model.Entity.Member;
import gift.Model.Entity.Wish;
import gift.Service.WishService;
import gift.annotation.ValidUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@Tag(name = "Wish", description = "Wish API")
@RestController
@RequestMapping("/api")
public class WishController {
    private final WishService wishService;


    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "찜 추가", description = "찜을 추가합니다")
    @ApiResponse(responseCode = "201", description = "추가 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @PostMapping("/wishes")
    public ResponseEntity<String> addWish(@ValidUser Member member, @Valid @RequestBody RequestWishDTO requestWishDTO) {
        Wish wish = wishService.addWish(member, requestWishDTO);
        return ResponseEntity.created(URI.create("api/wishes/" + wish.getId())).body("찜이 정상적으로 추가되었습니다");
    }

    @Operation(summary = "찜 조회", description = "찜 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 완료",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseWishDTO.class)))
            })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @GetMapping("/wishes")
    public ResponseEntity<List<ResponseWishDTO>> getWish(@ValidUser Member member) {
        List<ResponseWishDTO> response = wishService.getWish(member);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "찜 수정", description = "찜을 수정합니다")
    @ApiResponse(responseCode = "200", description = "수정 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @PutMapping("/wishes")
    public ResponseEntity<String> editWish(@ValidUser Member member, @Valid @RequestBody RequestWishDTO requestWishDTO) {
        wishService.editWish(member, requestWishDTO);
        return ResponseEntity.ok("찜이 정상적으로 수정되었습니다.");
    }

    @Operation(summary = "찜 삭제", description = "찜을 삭제합니다")
    @ApiResponse(responseCode = "200", description = "삭제 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @DeleteMapping("/wishes")
    public ResponseEntity<String> deleteWish(@ValidUser Member member, @Valid @RequestBody RequestWishDTO requestWishDTO) {
        wishService.deleteWish(member, requestWishDTO);

        return ResponseEntity.ok("찜이 정상적으로 삭제되었습니다");
    }
}