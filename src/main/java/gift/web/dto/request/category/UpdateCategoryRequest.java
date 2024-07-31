package gift.web.dto.request.category;

import gift.converter.StringToUrlConverter;
import gift.domain.Category;
import gift.domain.vo.Color;
import gift.web.validation.constraints.HexColor;
import gift.web.validation.constraints.SpecialCharacter;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public class UpdateCategoryRequest {

    @NotBlank
    @Length(min = 1, max = 15)
    @SpecialCharacter(allowed = "(, ), [, ], +, -, &, /, _")
    private String name;

    @NotBlank
    private String description;

    @URL
    private String imageUrl;

    @NotBlank
    @HexColor
    private String color;

    public UpdateCategoryRequest(String name, String description, String imageUrl, String color) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getColor() {
        return color;
    }

    public Category toEntity() {
        return new Category.Builder()
            .name(this.name)
            .description(this.description)
            .imageUrl(StringToUrlConverter.convert(this.imageUrl))
            .color(Color.from(this.color))
            .build();
    }

}
