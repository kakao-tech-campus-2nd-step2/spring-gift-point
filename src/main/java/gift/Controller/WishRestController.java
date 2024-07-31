package gift.Controller;

import gift.Model.request.WishRequest;
import gift.Model.response.WishResponse;
import gift.Service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "위시리스트 API", description = "위시리스트와 관련된 API")
@RestController
@RequestMapping("/api/wishes")
public class WishRestController {
    private final WishService wishService;

    public WishRestController(WishService wishService){
        this.wishService = wishService;
    }

    @Operation(summary = "위시리스트 추가", description = "이메일과 제품 명을 받아 wish 데이터베이스에 추가한다.")
    @Parameter(name="email", description = "사용자의 이메일로, intercepter에서 header에 있는 인증 토큰을 디코딩하여 email에 추가한다.")
    @Parameter(name="optionName", description = "제품명으로, product 데이터베이스에 있는 데이터이다.")
    @PostMapping()
    public ResponseEntity<?> create(@RequestAttribute("Email") String email, @RequestBody WishRequest wishRequest){
        wishService.create(email, wishRequest);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "위시리스트 제거", description = "email과 제품명을 받아 wish 데이터베이스에서 제거한다.")
    @Parameter(name="email", description = "사용자의 이메일로, intercepter에서 header에 있는 인증 토큰을 디코딩하여 email에 추가한다.")
    @Parameter(name="optionName", description = "제품명으로, product 데이터베이스에 있는 데이터이다.")
    @DeleteMapping("/{wishId}")
    public ResponseEntity<?> delete(@RequestAttribute("Email") String email, @PathVariable Long wishId){
        wishService.delete(email, wishId);
        return ResponseEntity.ok("success");
    }

    @Operation(summary = "위시리스트 조회", description = "email을 받아 wish 데이터베이스에 있는 데이터를 조회한다.")
    @GetMapping()
    public Page<WishResponse> readPage(@RequestAttribute("Email") String email, @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                       @RequestParam(value = "sort", defaultValue = "10") String sort){
        return wishService.getPage(email, page, size, sort);
    }
}
