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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option", description = "옵션 관련 api")
public class OptionController {

    private final OptionService optionService;
    private final KakaoTalkService kakaoTalkService;

    @Autowired
    public OptionController(OptionService optionService, KakaoTalkService kakaoTalkService) {
        this.optionService = optionService;
        this.kakaoTalkService = kakaoTalkService;
    }

    @GetMapping
    @Operation(summary = "상품 옵션 목록 조회", description = "상품에 대한 옵션을 보여줍니다.")
    public ResponseEntity<List<OptionDto>> getAllOptionsByProductId(@PathVariable Long productId) {
        List<OptionDto> options = optionService.getAllOptionsByProductId(productId);
        if (options.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(options);
    }

    @PostMapping
    @Operation(summary = "상품 옵션 추가", description = "상품에 대한 옵션을 추가합니다.")
    public ResponseEntity<?> addOption(@RequestBody OptionDto optionDto) {
        OptionDto savedOptionDto = optionService.addOption(optionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOptionDto);
    }
/*
    //이건 필요한가?
    @GetMapping("/{optionId}")
    @Operation(summary = "옵션 수정 화면", description = "옵션 수정 화면을 보여줍니다.")
    public ResponseEntity<?> updateOption(@PathVariable long productId, @PathVariable long optionId) {
        try {
            optionService.getOptionById(optionId);
            ClassPathResource resource = new ClassPathResource("templates/option_form.html");
            String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return new ResponseEntity<>(htmlContent, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error loading login page", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

 */

    @PutMapping("/{optionId}")
    @Operation(summary = "상품 옵션 수정", description = "상품에 대한 옵션을 수정합니다.")
    public ResponseEntity<?> updateOption(@PathVariable long productId, @PathVariable long optionId, @RequestBody OptionDto optionDto) {
        try {
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

    @DeleteMapping("/{optionId}")
    @Operation(summary = "상품 옵션 삭제", description = "상품에 대한 옵션을 삭제합니다.")
    public ResponseEntity<?> deleteOption(@PathVariable long optionId) {
        try {
            optionService.deleteOption(optionId);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>("Option updated successfully", headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting option");
        }
    }


}
