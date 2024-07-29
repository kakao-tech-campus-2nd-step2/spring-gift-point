package gift.controller;

import gift.annotation.KakaoUser;
import gift.dto.*;
import gift.service.KakaoApiService;
import gift.service.OptionService;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/kakao/wish")
@Tag(name = "Kakao Wish API", description = "카카오 위시리스트 및 주문 관련 API")
public class KakaoOrderController {
    private final WishService wishService;
    private final OptionService optionService;
    private final KakaoApiService kakaoApiService;

    public KakaoOrderController(WishService wishService, OptionService optionService, KakaoApiService kakaoApiService) {
        this.wishService = wishService;
        this.kakaoApiService = kakaoApiService;
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    public String getKakaoWishes(@KakaoUser KakaoUserDTO kakaoUserDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        WishPageResponseDTO wishOptions = wishService.getWishlist(kakaoUserDTO.user().getId(), pageable);
        model.addAttribute("wishOptions", wishOptions);

        return "kakaoWishlist";
    }

    @PostMapping("/order")
    @Operation(summary = "주문 생성",
            description = "옵션 수량 차감, 위시리스트에서 삭제, 메시지 전송",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    public ResponseEntity<Void> handleKakaoOrder(@KakaoUser KakaoUserDTO kakaoUserDTO, @RequestBody OrderRequestDTO orderRequestDTO) {
        optionService.subtractOptionQuantity(orderRequestDTO.optionId(), orderRequestDTO.quantity());
        wishService.deleteWishOption(kakaoUserDTO.user().getId(), orderRequestDTO.optionId());
        kakaoApiService.sendMessage(kakaoUserDTO.accessToken(), orderRequestDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/addWish")
    @Operation(summary = "위시리스트에 옵션 추가 페이지",
            responses = {
                    @ApiResponse(responseCode = "200", description = "옵션 추가 페이지 로드 성공"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    public String addWishOptionPage(@KakaoUser KakaoUserDTO kakaoUserDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        OptionsPageResponseDTO options = optionService.getAllOptions(pageable);
        model.addAttribute("options", options);

        return "addKakaoWishOption"; // addWishProduct.html로 이동
    }

    @PostMapping("/addWish")
    @Operation(summary = "위시리스트에 옵션 추가",
            responses = {
                    @ApiResponse(responseCode = "200", description = "옵션 추가 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    public ResponseEntity<String> addWishOption(@KakaoUser KakaoUserDTO kakaoUserDTO, @RequestBody WishRequestDTO wishRequestDTO, Model model) {
        wishService.addWishOption(kakaoUserDTO.user().getId(), wishRequestDTO);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "위시리스트에서 옵션 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "옵션 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    public String deleteWishOption(@KakaoUser KakaoUserDTO kakaoUserDTO, @RequestBody WishRequestDTO wishRequestDTO, Model model, @PageableDefault(size = 3) Pageable pageable) {
        wishService.deleteWishOption(kakaoUserDTO.user().getId(), wishRequestDTO.optionId());

        WishPageResponseDTO wishOptions = wishService.getWishlist(kakaoUserDTO.user().getId(), pageable);
        model.addAttribute("wishOptions", wishOptions);

        return "kakaoWishlist";
    }
}
