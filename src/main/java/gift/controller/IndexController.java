package gift.controller;

import gift.config.KakaoProperties;
import gift.dto.CategoryDTO;
import gift.dto.productDTOs.ProductDTO;
import gift.service.CategoryService;
import gift.service.KakaoAuthService;
import gift.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    private final KakaoAuthService kakaoAuthService;
    private final KakaoProperties kakaoProperties;

    private final ProductService productService;
    private final CategoryService categoryService;

    public IndexController(KakaoAuthService kakaoAuthService, KakaoProperties kakaoProperties, ProductService productService, CategoryService categoryService) {
        this.kakaoAuthService = kakaoAuthService;
        this.kakaoProperties = kakaoProperties;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/products";
    }

    @GetMapping("/postform")
    public String postform(Model model){
        List<String> categoryList = getCateogoryList();
        model.addAttribute("categoryList", categoryList);
        return "postform";
    }

    @PostMapping("/editform/{id}")
    public String editform(@PathVariable Long id, Model model){
        ProductDTO product = productService.getProductDTOById(id);
        model.addAttribute("product", product);
        List<String> categoryList = getCateogoryList();
        model.addAttribute("categoryList", categoryList);
        return "editform";
    }

    private List<String> getCateogoryList(){
        List<CategoryDTO> categories = categoryService.getAllCategories();
        List<String> GetCategoryList = categories.stream()
                .map(CategoryDTO::getName)
                .toList();
        List<String> categoryList = new ArrayList<>();
        categoryList.addAll(GetCategoryList);
        categoryList.remove("NONE");
        return categoryList;
    }

    @GetMapping(value="/oauth/kakao")
    public String kakaoConnect() {
        logger.info("redirect_uri={}", kakaoProperties.getRedirectUrl());
        logger.info("client_id=" + kakaoProperties.getClientId());
        String url = UriComponentsBuilder.fromHttpUrl(kakaoProperties.getLoginUrl())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUrl())
                .queryParam("client_id", kakaoProperties.getClientId())
                .build()
                .toUriString();
        return "redirect:" + url;
    }
}
