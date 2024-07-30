package gift.controller;

import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "wish", description = "위시 API")
@RequestMapping("/wish")
@Controller
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping()
    @Operation(summary = "위시리스트에 상품 추가", description = "위시리스트에 전달받은 상품정보를 위시로 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시 등록 성공", content = @Content(schema = @Schema(implementation = WishResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<WishResponseDto> save(@RequestBody WishRequestDto wishRequestDto) {
        WishResponseDto wishResponseDto = wishService.save(wishRequestDto.getProductId(), wishRequestDto.getTokenValue());
        return new ResponseEntity<>(wishResponseDto, HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "멤버가 가진 모든 위시를 조회", description = "토큰값을 이용하여 멤버를 찾고, 해당 멤버의 모든 위시를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시 조회 성공", content = @Content(schema = @Schema(implementation = WishResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<List<WishResponseDto>> getAll(@RequestParam("Token") String token) {
        return new ResponseEntity<>(wishService.getAll(token), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "위시 삭제", description = "위시id를 이용하여, 해당 아이디값을 가진 위시를 삭제합니다.")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IllegalAccessException {
        wishService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/wishes")
    @Operation(summary = "페이지네이션을 적용하여 모든 위시 조회", description = "모든 위시를 조회하되, 페이지네이션을 적용하여 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "페이지네이션으로 위시 목록 조회 성공", content = @Content(schema = @Schema(implementation = WishResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<Page<WishResponseDto>> getWishes(Pageable pageable) {
        return new ResponseEntity<>(wishService.getWishes(pageable), HttpStatus.OK);
    }

}
