package gift.controller;

import gift.annotation.LoginMember;
import gift.constants.ResponseMsgConstants;
import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.ResponseDTO;
import gift.dto.betweenClient.wish.WishRequestDTO;
import gift.dto.betweenClient.wish.WishResponseDTO;
import gift.dto.swagger.GetWishListResponse;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/wishes")
public class WishListController {

    private final WishListService wishListService;

    @Autowired
    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    @Operation(description = "서버가 클라이언트에게 요청한 회원의 위시리스트를 제공합니다." +
            " 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.", tags = "WishList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 위시리스트를 제공합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetWishListResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청의 양식이 잘못된 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "유효하지 않는 토큰입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public Page<WishResponseDTO> getWishes(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable) {
        return wishListService.getWishList(memberDTO, pageable);
    }

    @PostMapping
    @Operation(description = "서버가 클라이언트가 제출한 위시리스트 항목을 추가합니다." +
            " 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.", tags = "WishList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "위시리스트 항목 추가에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(value = "{\"isError\": false, \"message\": \"success\"}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 요청의 양식이 잘못된 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "유효하지 않는 토큰입니다.",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> addWishes(@RequestBody @Valid WishRequestDTO wishRequestDTO, @Parameter(hidden = true) @LoginMember MemberDTO memberDTO) {
        wishListService.addWishes(memberDTO, wishRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "서버가 클라이언트가 제출한 위시리스트 항목을 삭제합니다." +
            " 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.", tags = "WishList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "위시리스트 항목 삭제에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(value = "{\"isError\": false, \"message\": \"success\"}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 존재하지 않는 ID인 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "유효하지 않는 토큰입니다.",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> deleteWishes(@PathVariable @Min(1) @NotNull Long id,
            @Parameter(hidden = true) @LoginMember MemberDTO memberDTO) {
        wishListService.removeWishListProduct(memberDTO, id);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{quantity}")
    @Hidden
    public ResponseEntity<ResponseDTO> setWishes(@PathVariable @Min(0) @NotNull Integer quantity, @Parameter(hidden = true) @LoginMember MemberDTO MemberDTO,
            @RequestBody @Valid WishRequestDTO wishRequestDTO) {
        wishListService.setWishListNumber(MemberDTO, wishRequestDTO, quantity);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.OK);
    }
}
