package gift.web.dto.request.product;

import gift.converter.StringToUrlConverter;
import gift.domain.Product;
import gift.web.validation.constraints.RequiredKakaoApproval;
import gift.web.validation.constraints.SpecialCharacter;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

public class UpdateProductRequest {

    @NotBlank
    @Length(min = 1, max = 15)
    @SpecialCharacter(allowed = "(, ), [, ], +, -, &, /, _")
    @RequiredKakaoApproval
    private final String name;
    @Range(min = 1000, max = 10000000)
    private final Integer price;

    @URL
    private final String imageUrl;

    public UpdateProductRequest(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product toEntity() {
        return new Product.Builder()
            .name(name)
            .price(price)
            .imageUrl(StringToUrlConverter.convert(imageUrl))
            .build();
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
}
