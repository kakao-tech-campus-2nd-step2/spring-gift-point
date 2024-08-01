package gift.domain.exception.badRequest;

import gift.domain.exception.ErrorCode;

public class OauthVendorIllegalException extends BadRequestException {

    public OauthVendorIllegalException() {
        super(ErrorCode.OAUTH_VENDOR_ILLEGAL);
    }
}
