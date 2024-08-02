package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishPageResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.User;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wishes", description = "위시리스트 관련 API")
public class WishController {
    private final WishService wishService;
    private static final int DEFAULT_SIZE = 20;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "위시리스트에 새로운 상품을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "위시리스트 항목이 성공적으로 추가되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = WishResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 상품을 찾을 수 없는 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<WishResponseDto> addWish(
            @LoginMember User loginUser,
            @Parameter(description = "추가할 위시리스트 항목의 정보", required = true) @RequestBody WishRequestDto wishRequestDto) {
        WishResponseDto createdWish = wishService.addWish(loginUser.getId(), wishRequestDto);
        return new ResponseEntity<>(createdWish, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "사용자의 모든 위시리스트 항목을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시리스트 항목이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = WishPageResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<WishPageResponseDto> getWishesByUserId(
            @LoginMember User loginUser,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false, schema = @Schema(type = "integer", defaultValue = "0")) @RequestParam(defaultValue = "0") @Min(0) int page) {
        WishPageResponseDto wishList = wishService.getWishesByUserId(loginUser.getId(), page, DEFAULT_SIZE);
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "위시리스트 항목 삭제", description = "위시리스트에서 특정 상품을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "위시리스트 항목이 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "위시리스트 항목을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteWish(
            @Parameter(description = "삭제할 위시리스트 항목 ID", required = true) @PathVariable Long id) {
        wishService.deleteWish(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
