package gift.global.dto;

import gift.global.model.PageInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

// 페이지 관련 정보를 담을 dto. resolver가 아닌 controller에서 처리하도록 리팩토링
public record PageInfoDto(
    @Min(value = 0, message = "페이지는 음수일 수 없습니다.")
    Integer pageNumber,

    @Min(value = 1, message = "한 페이지 당 1개 이상의 제품이 있어야 합니다.")
    Integer pageSize,

    String sortProperty,

    @Pattern(regexp = "^(ASC|DESC)$")
    String sortDirection
) {

    public PageInfo toPageInfo() {
        return new PageInfo(pageNumber, pageSize, sortProperty, sortDirection);
    }
}
