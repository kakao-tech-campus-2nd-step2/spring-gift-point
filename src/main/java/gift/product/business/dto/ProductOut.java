package gift.product.business.dto;

import gift.product.persistence.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;

public class ProductOut {

    public record Init(
        Long id,
        String name,
        String description,
        Integer price,
        String url
    ) {

        public static Init from(Product product) {
            return new Init(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getUrl()
            );
        }

        public static List<Init> of(List<Product> content) {
            return content.stream()
                .map(Init::from)
                .toList();
        }
    }

    public record WithOptions(
        Long id,
        String name,
        String description,
        Integer price,
        String url,
        List<OptionOut.Info> options
    ) {

        public static WithOptions from(Product product) {
            return new WithOptions(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getUrl(),
                OptionOut.Info.of(product.getOptions())
            );
        }

    }

    public record Paging(
        boolean hasNext,
        List<Init> productList
    ) {

        public static Paging from(Page<Product> productsPage) {
            return new Paging(
                productsPage.hasNext(),
                Init.of(productsPage.getContent())
            );
        }

    }

}
