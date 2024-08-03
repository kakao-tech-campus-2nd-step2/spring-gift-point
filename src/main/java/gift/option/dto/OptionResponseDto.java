package gift.option.dto;

import gift.option.entity.Option;

// 옵션 목록을 보여줄 때 사용할 dto
public record OptionResponseDto(
    long optionId,
    String name,
    int quantity,
    long productId
) {

    // 코드 중복을 줄이고자 entity를 넣어도 dto로 변환되게 하였습니다.
    public static OptionResponseDto fromOption(Option option) {
        return new OptionResponseDto(option.getOptionId(), option.getName(), option.getQuantity(),
            option.getproductId());
    }

    // dto -> entity 메서드도 dto에 넣었는데, 상대적으로 dto가 코드 변경이 잦으므로 한 번에 관리하기 위해 이런 방법을 사용하였습니다.
    public Option toOption() {
        return new Option(optionId, name, quantity, productId);
    }
}
