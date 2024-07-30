package gift.domain.wish;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import gift.domain.member.dto.LoginInfo;
import gift.domain.wish.dto.WishPageResponse;
import gift.domain.wish.dto.WishResponse;
import gift.global.resolver.Login;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/members/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public String wishPage(
        Model model,
        @PageableDefault(page = 0, sort = "id_asc")
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @Parameter(description = "정렬 기준") @RequestParam(value = "sort", defaultValue = "createdDate_desc") String sort,
        @Login LoginInfo loginInfo) {
        Sort sortObj = getSortObject(sort);
        PageRequest pageRequest = PageRequest.of(page, size, sortObj);
        WishPageResponse wishPageResponse = wishService.getProductsInWish(loginInfo.getId(),
            pageRequest);

        model.addAttribute("wishes", wishPageResponse);
        return "wish";
    }

    private Sort getSortObject(String sort) {
        switch (sort) {
            case "price_asc":
                return Sort.by(ASC, "price");
            case "price_desc":
                return Sort.by(DESC, "price");
            case "createdDate_asc":
                return Sort.by(ASC, "createdDate");
            default:
                return Sort.by(DESC, "createdDate");
        }
    }
}

