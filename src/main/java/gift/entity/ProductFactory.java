package gift.entity;

import org.springframework.stereotype.Component;

@Component
public class ProductFactory {

    public Product createProduct() {
        return new Product();
    }
}
