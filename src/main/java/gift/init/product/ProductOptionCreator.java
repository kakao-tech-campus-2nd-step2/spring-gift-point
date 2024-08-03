package gift.init.product;

import gift.domain.product.ProductOption.CreateOption;
import gift.service.product.ProductOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductOptionCreator {

    private final ProductOptionService productOptionService;

    @Autowired
    public ProductOptionCreator(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    public void creator() {
        productOptionService.createProductOption(1L, new CreateOption("option20", 100L));
        productOptionService.createProductOption(2L, new CreateOption("option20", 100L));
        productOptionService.createProductOption(3L, new CreateOption("option20", 100L));

        productOptionService.createProductOption(2L, new CreateOption("option30", 100L));
        productOptionService.createProductOption(3L, new CreateOption("option30", 100L));
    }
}
