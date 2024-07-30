package gift.Controller;

import gift.Annotation.LoginMemberResolver;
import gift.Model.MemberDto;
import gift.Model.OptionDto;
import gift.Model.OrderRequestDto;
import gift.Service.KakaoTalkService;
import gift.Service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "Option", description = "옵션 관련 api")
public class OptionController {

    private final OptionService optionService;
    private final KakaoTalkService kakaoTalkService;

    @Autowired
    public OptionController(OptionService optionService, KakaoTalkService kakaoTalkService) {
        this.optionService = optionService;
        this.kakaoTalkService = kakaoTalkService;
    }

    @GetMapping("/api/products/options/{productId}")
    @Operation(summary = "옵션 표출", description = "상품에 대한 옵션을 보여줍니다.")
    public ResponseEntity<List<OptionDto>> getAllOptionsByProductId(@PathVariable Long productId) {
        List<OptionDto> options = optionService.getAllOptionsByProductId(productId);
        if (options.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(options);
    }

    @GetMapping("/api/products/options/add/{productId}")
    @Operation(summary = "옵션 추가 화면", description = "옵션 추가 화면을 보여줍니다.")
    public String addOption(Model model, @PathVariable("productId") long productId) {
        OptionDto optionDto = new OptionDto();
        optionDto.setProductId(productId);
        model.addAttribute("optionDto", optionDto);
        return "option_form";
    }

    @PostMapping("/api/products/options/add")
    @Operation(summary = "옵션 추가", description = "상품에 대한 옵션을 추가합니다.")
    public String addOption(@ModelAttribute OptionDto optionDto, Model model, @RequestParam("productId") long productId) {
        model.addAttribute("optionDto", optionDto);
        optionDto.setProductId(productId);
        optionService.addOption(optionDto);
        return "redirect:/api/products";
    }

    @GetMapping("/api/products/options/{productId}/{optionId}/update")
    @Operation(summary = "옵션 수정 화면", description = "옵션 수정 화면을 보여줍니다.")
    public String updateOption(Model model, @PathVariable long productId, @PathVariable long optionId) {
        OptionDto optionDto = optionService.getOptionById(optionId);
        model.addAttribute("optionDto", optionDto);
        return "option_form";
    }

    @PostMapping("/api/products/options/{productId}/{optionId}/update")
    @Operation(summary = "옵션 수정", description = "상품에 대한 옵션을 수정합니다.")
    public ResponseEntity<?> updateOption(@PathVariable long productId, @PathVariable long optionId, @ModelAttribute OptionDto optionDto, Model model) {
        try {
            model.addAttribute("optionDto", optionDto);
            optionDto.setProductId(productId);
            optionDto.setId(optionId);
            optionService.updateOption(optionDto);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/products");
            return new ResponseEntity<>("Option updated successfully", headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating option");
        }
    }

    @DeleteMapping("/api/products/options/{optionId}/delete")
    @Operation(summary = "옵션 삭제", description = "상품에 대한 옵션을 삭제합니다.")
    public ResponseEntity<?> deleteOption(@PathVariable long optionId) {
        try {
            optionService.deleteOption(optionId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/products");
            return new ResponseEntity<>("Option updated successfully", headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting option");
        }
    }

    @PostMapping("/option/purchase")
    @Operation(summary = "위시리스트 구매", description = "위시리스트에 있는 상품을 구매합니다.")
    public ResponseEntity<String> purchaseWishlist(@LoginMemberResolver MemberDto memberDto, @RequestBody List<OrderRequestDto> orderRequestDtoList, HttpServletRequest request) {
        for (OrderRequestDto orderRequestDto : orderRequestDtoList) {
            orderRequestDto.setMemberId(memberDto.getId());
        }
        optionService.subtractOption(orderRequestDtoList);

        //카카오 토큰 가져오기
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        //토큰으로 메시지 보내기
        if (token != null) { //
            try {
                kakaoTalkService.sendMessageToMe(token, orderRequestDtoList);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error sending message");
            }
        }

        return ResponseEntity.ok("Purchase successful");
    }
}
