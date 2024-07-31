package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;

public class OauthVendorIllegalException extends BadRequestException {

    public OauthVendorIllegalException() {
        super("Oauth 공급자가 잘못되었습니다. 관리자에게 문의하세요.", ErrorCode.OAUTH_VENDOR_ILLEGAL);
    }
}
