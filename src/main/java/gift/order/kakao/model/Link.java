package gift.order.kakao.model;

import gift.global.model.MultiValueMapConvertibleDto;

public record Link(
    String webUrl,
    String mobileWebUrl
) implements MultiValueMapConvertibleDto {

}
