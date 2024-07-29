package gift.product.presentation.dto;

import gift.product.business.dto.ProductOut;
import java.util.List;

public class ProductResponse {

    public record Info(
        Long id,
        String name,
        Integer price,
        String description,
        String imageUrl
    ) {

        public static Info from(ProductOut.Init productOutInit) {
            return new Info(
                productOutInit.id(),
                productOutInit.name(),
                productOutInit.price(),
                productOutInit.description(),
                productOutInit.url()
            );
        }

        public static List<Info> of(List<ProductOut.Init> products) {
            return products.stream()
                .map(Info::from)
                .toList();
        }
    }

    public record PagingInfo(
        boolean hasNext,
        List<Info> productList
    ) {

        public static PagingInfo from(ProductOut.Paging productOutPaging) {
            return new PagingInfo(
                productOutPaging.hasNext(),
                Info.of(productOutPaging.productList())
            );
        }
    }

    public record WithOptions(
        Long id,
        String name,
        Integer price,
        String description,
        String imageUrl,
        List<OptionResponse.Info> options
    ) {

        public static WithOptions from(ProductOut.WithOptions productOut) {
            return new WithOptions(
                productOut.id(),
                productOut.name(),
                productOut.price(),
                productOut.description(),
                productOut.url(),
                OptionResponse.Info.of(productOut.options())
            );
        }
    }
}
