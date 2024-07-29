package gift.controller;

import gift.dto.PageRequestDTO;
import gift.dto.WishListDTO;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/wishlist")
@Validated
@Tag(name = "WishList Management", description = "APIs for managing wishlists")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    @Operation(summary = "View wishlist", description = "This API retrieves the wishlist with pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved wish list."),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters."),
        @ApiResponse(responseCode = "401", description = "User not logged in."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String viewWishList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        HttpServletRequest request, Model model) {

        String email = (String) request.getAttribute("email");
        if (email == null) {
            return "redirect:/users/login";
        }

        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, size, sortBy, direction);
        Page<WishListDTO> wishListPage = wishListService.getWishListByUser(email, pageRequestDTO);

        // 모델에 데이터 추가
        model.addAttribute("wishList", wishListPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", wishListPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "wishlist";
    }

    @GetMapping("/add")
    @Operation(summary = "Show add product form", description = "This API returns the form to add a new product to the wishlist.")
    public String showAddProductForm(Model model) {
        model.addAttribute("productId", new Long(0));
        return "add_product_to_wishlist";
    }

    @PostMapping("/add")
    @Operation(summary = "Add a product to wishlist", description = "This API adds a new product to the wishlist.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully added product to wish list."),
        @ApiResponse(responseCode = "400", description = "Invalid product data."),
        @ApiResponse(responseCode = "401", description = "User not logged in."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String addProductToWishList(@RequestBody Map<String, Long> payload, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        Long productId = payload.get("productId");
        wishListService.addProductToWishList(email, productId);
        return "redirect:/wishlist";
    }

    @DeleteMapping("/remove/{productId}")
    @Operation(summary = "Remove a product from wishlist", description = "This API removes a product from the wishlist.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully removed product from wish list."),
        @ApiResponse(responseCode = "400", description = "Invalid product data."),
        @ApiResponse(responseCode = "401", description = "User not logged in."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String removeProductFromWishList(@PathVariable Long productId, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        wishListService.removeProductFromWishList(email, productId);
        return "redirect:/wishlist";
    }
}