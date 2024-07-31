package gift.controller.api;

import gift.dto.Request.WishRequestDto;
import gift.dto.Response.WishResponseDto;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/api/wishes")
@Tag(name = "Wish API", description = "위시리스트 관련 API")
public class WishlistController {

    private final WishlistService wishService;

    @Autowired
    public WishlistController(WishlistService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/add")
    @Operation(summary = "위시리스트 추가", description = "회원의 위시리스트에 상품을 추가합니다.")
    public String addToWish(@RequestBody WishRequestDto wishRequestDto, Principal principal) {
        String username = principal.getName();
        wishService.addToWish(username, wishRequestDto);
        return "redirect:/api/wishes";
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트 삭제", description = "회원의 위시리스트에서 상품을 삭제합니다.")
    public String removeFromWish(@PathVariable Long wishId) {
        wishService.removeFromWish(wishId);
        return "redirect:/api/wishes";
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "회원의 위시리스트를 페이지 단위로 조회합니다.")
    public String getWishes(Principal principal,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdDate") String sort,
        @RequestParam(defaultValue = "desc") String direction,
        Model model) {
        String username = principal.getName();
        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<WishResponseDto> wishlistPage = wishService.getWishesByUser(username, pageable);
        model.addAttribute("wishlistPage", wishlistPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        return "wishlist";
    }
}
