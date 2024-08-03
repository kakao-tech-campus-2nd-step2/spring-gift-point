package gift.order.kakao.model;

import gift.global.model.MultiValueMapConvertibleDto;

public record TemplateObject(
    String objectType,
    String text,
    Link link,
    String buttonTitle
) implements MultiValueMapConvertibleDto {

}
