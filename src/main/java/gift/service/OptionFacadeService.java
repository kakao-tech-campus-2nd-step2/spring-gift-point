package gift.service;

import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wish;
import gift.util.KakaoApiUtil;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionFacadeService {

    private final OptionService optionService;
    private final ProductService productService;

    private final WishlistService wishlistService;

    private final KakaoApiUtil kakaoApiUtil;
    private final SnsMemberService snsMemberService;

    public OptionFacadeService(OptionService optionService, ProductService productService,
        WishlistService wishlistService, KakaoApiUtil kakaoApiUtil,
        SnsMemberService snsMemberService) {
        this.optionService = optionService;
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.kakaoApiUtil = kakaoApiUtil;
        this.snsMemberService = snsMemberService;
    }


    public Product findProductById(Long id) {
        return productService.getProductById(id);
    }

    public void addOption(Option option) {
        optionService.addOption(option);
    }

    public void updateOption(Option option, Long id) {
        optionService.updateOption(option, id);
    }

    public void deleteOption(Long id) {
        optionService.deleteOption(id);
    }

    public List<Option> getAllProductOption(Long id) {
        return optionService.getOptionByProductId(id);
    }




}
