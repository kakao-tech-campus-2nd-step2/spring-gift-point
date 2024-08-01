package gift.controller;

import gift.config.LoginUser;
import gift.dto.ResponseMessage;
import gift.dto.WishRequest;
import gift.dto.WishResponseForPage;
import gift.entity.User;
import gift.entity.Wish;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/wishes")
@Tag(name = "Wish Management", description = "APIs for managing wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    @Operation(summary = "위시 생성", description = "새로운 위시를 생성합니다.",
        responses = @ApiResponse(responseCode = "201", description = "위시 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wish.class))))
    public ResponseEntity<Wish> create(@RequestBody WishRequest request,
        @Parameter(hidden = true) @LoginUser User user) {
        Wish wish = wishService.addWish(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(wish);
    }

    @GetMapping
    @Operation(summary = "사용자 모든 위시 조회", description = "사용자의 모든 위시를 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "위시 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))))
    public Page<WishResponseForPage> getWishes(@Parameter(hidden = true) @LoginUser User user,
        @ParameterObject @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return wishService.getWishes(user.getId(), pageable);
    }

    @GetMapping("/{wishId}")
    @Operation(summary = "위시 ID로 위시 조회", description = "위시 ID에 해당하는 특정 위시를 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "위시 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Wish.class))))
    public Wish getOneWish(@PathVariable Long wishId,
        @Parameter(hidden = true) @LoginUser User user) {
        return wishService.getWishById(user.getId(), wishId);
    }

//    @PutMapping("/{wishId}")
//    @Operation(summary = "위시 업데이트", description = "위시 ID에 해당하는 위시를 업데이트합니다.",
//        responses = @ApiResponse(responseCode = "200", description = "위시 업데이트 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))))
//    public ResponseEntity<ResponseMessage> updateNumber(@PathVariable Long wishId,
//        @Parameter(hidden = true) @LoginUser User user, @RequestBody WishRequest wishRequest) {
//        wishService.updateNumber(user.getId(), wishId, wishRequest.getNumber());
//        ResponseMessage responseMessage = new ResponseMessage("수정되었습니다.");
//        return ResponseEntity.ok(responseMessage);
//    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시 삭제", description = "위시 ID에 해당하는 위시를 삭제합니다.",
        responses = @ApiResponse(responseCode = "200", description = "위시 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))))
    public ResponseEntity<ResponseMessage> delete(@PathVariable Long wishId,
        @Parameter(hidden = true) @LoginUser User user) {
        wishService.removeWish(user.getId(), wishId);
        ResponseMessage responseMessage = new ResponseMessage("삭제되었습니다.");
        return ResponseEntity.ok(responseMessage);
    }
}
