package gift.init;

import gift.domain.Product.CreateProduct;
import gift.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductCreator {


    private final ProductService productService;

    @Autowired
    public ProductCreator(ProductService productService) {
        this.productService = productService;
    }

    public void creator() {
        productService.createProduct(
            new CreateProduct("Product A", 1000, "image1.jpg", 1L, "option1", 1L));
        productService.createProduct(
            new CreateProduct("Product B", 1500, "image2.jpg", 2L, "option2", 10L));
        productService.createProduct(
            new CreateProduct("Product C", 2000, "image3.jpg", 3L, "option3", 100L));

        productService.createProduct(new CreateProduct("커피", 100,
            "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
            1L, "option4", 1000L));
        productService.createProduct(new CreateProduct("콜라", 2000,
            "https://img.danawa.com/prod_img/500000/059/749/img/13749059_1.jpg?_v=20220524145210",
            2L, "option5", 12L));
        productService.createProduct(new CreateProduct("몬스터", 1000,
            "https://img.danawa.com/prod_img/500000/658/896/img/17896658_1.jpg?_v=20220923092758",
            3L, "option6", 10L));
    }
}
