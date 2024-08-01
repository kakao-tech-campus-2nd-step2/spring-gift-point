package gift.web.dto.request.product;

import gift.converter.StringToUrlConverter;
import gift.domain.Category;
import gift.domain.Product;
import gift.domain.ProductOption;
import gift.web.dto.request.productoption.CreateProductOptionRequest;
import gift.web.validation.constraints.RequiredKakaoApproval;
import gift.web.validation.constraints.SpecialCharacter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

public class CreateProductRequest {

    @NotBlank
    @Length(min = 1, max = 15)
    @SpecialCharacter(allowed = "(, ), [, ], +, -, &, /, _")
    @RequiredKakaoApproval
    private final String name;

    @Range(min = 1000, max = 10000000)
    private final Integer price;

    @URL
    private final String imageUrl;

    @NotNull
    private final Long categoryId;

    @Valid
    @NotEmpty
    private final List<CreateProductOptionRequest> productOptions;

    public CreateProductRequest(String name, Integer price, String imageUrl, Long categoryId, List<CreateProductOptionRequest> productOptions) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.productOptions = productOptions;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<CreateProductOptionRequest> getProductOptions() {
        return productOptions;
    }

    public Product toEntity(Category category) {
        List<ProductOption> productOptions = this.productOptions.stream()
            .map(CreateProductOptionRequest::toEntity)
            .toList();

        return new Product.Builder()
            .name(this.name)
            .price(this.price)
            .imageUrl(StringToUrlConverter.convert(this.imageUrl))
            .category(category)
            .productOptions(productOptions)
            .build();
    }
}
