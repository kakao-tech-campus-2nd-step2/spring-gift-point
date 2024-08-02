package gift.dto.product;

import gift.dto.option.OptionDto;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public record AddProductRequest(

    @NotNull
    ProductDto productDto,

    @NotNull
    List<OptionDto> optionDtos
) {

    @AssertTrue(message = "상품에는 하나 이상의 옵션이 있어야 합니다.")
    public boolean hasOption() {
        return optionDtos == null || optionDtos().size() > 0;
    }

    @AssertTrue(message = "동일한 상품 내의 옵션 이름은 중복될 수 없습니다.")
    public boolean isOptionNameUnique() {
        return optionDtos == null || optionDtos.stream()
            .map(optionDto -> optionDto.name())
            .collect(Collectors.toSet()).size() == optionDtos.size();
    }
}