package gift.controller;

import gift.annotation.LoginMember;
import gift.constants.ResponseMsgConstants;
import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.product.ProductRequestDTO;
import gift.dto.betweenClient.ResponseDTO;
import gift.dto.betweenClient.wish.WishDTO;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = WishDTO.class)))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public String getWishes(Model model, @Parameter(hidden = true) @LoginMember MemberDTO memberDTO, Pageable pageable) {
        Page<WishDTO> wishDTOPage = wishListService.getWishList(memberDTO, pageable);
        model.addAttribute("wishDTOPage", wishDTOPage);
        return "getWishes";
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
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> addWishes(@RequestBody @Valid ProductRequestDTO productRequestDTO,
            @Parameter(hidden = true) @LoginMember MemberDTO memberDTO) {
        wishListService.addWishes(memberDTO, productRequestDTO);
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
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> deleteWishes(@PathVariable @Min(1) @NotNull Long id,
            @Parameter(hidden = true) @LoginMember MemberDTO memberDTO) {
        wishListService.removeWishListProduct(memberDTO, id);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{quantity}")
    @Operation(description = "서버가 클라이언트가 제출한 위시리스트 항목의 수량을 수정합니다." +
            " 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.", tags = "WishList")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위시리스트 항목 수정에 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(value = "{\"isError\": false, \"message\": \"success\"}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 요청의 양식이 잘못되었거나, 존재하지 않는 항목인 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> setWishes(@PathVariable @Min(0) @NotNull Integer quantity, @Parameter(hidden = true) @LoginMember MemberDTO MemberDTO,
            @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        wishListService.setWishListNumber(MemberDTO, productRequestDTO, quantity);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.OK);
    }
}
