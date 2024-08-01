package gift.product.dto;

import gift.option.entity.Option;

// 제품의 옵션을 보여줄 dto (기존의 옵션 dto는 product 정보가 나와서, 제품의 옵션을 보여줄 때 제품과 중복됨)
public record OptionExceptProductResponseDto(
    long optionId,
    String name,
    int quantity) {

    public static OptionExceptProductResponseDto fromOption(Option option) {
        return new OptionExceptProductResponseDto(option.getOptionId(), option.getName(),
            option.getQuantity());
    }
}
