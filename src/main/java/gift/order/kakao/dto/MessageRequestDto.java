package gift.order.kakao.dto;

import gift.global.model.MultiValueMapConvertibleDto;
import gift.order.kakao.model.TemplateObject;

// 메시지 보내기 요청 api의 body dto
public record MessageRequestDto(
    TemplateObject templateObject
) implements MultiValueMapConvertibleDto {

}
