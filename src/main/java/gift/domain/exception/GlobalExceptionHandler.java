package gift.domain.exception;

import com.google.common.base.CaseFormat;
import gift.domain.exception.badRequest.BadRequestException;
import gift.domain.exception.badRequest.OauthVendorIllegalException;
import gift.domain.exception.badRequest.OptionQuantityOutOfRangeException;
import gift.domain.exception.badRequest.OptionUpdateActionInvalidException;
import gift.domain.exception.badRequest.ProductOptionsEmptyException;
import gift.domain.exception.conflict.CategoryAlreadyExistsException;
import gift.domain.exception.conflict.CategoryHasProductsException;
import gift.domain.exception.conflict.ConflictException;
import gift.domain.exception.conflict.MemberAlreadyExistsException;
import gift.domain.exception.conflict.OptionAlreadyExistsInProductException;
import gift.domain.exception.conflict.ProductAlreadyExistsException;
import gift.domain.exception.forbidden.ForbiddenException;
import gift.domain.exception.forbidden.MemberIncorrectLoginInfoException;
import gift.domain.exception.forbidden.MemberNotAdminException;
import gift.domain.exception.unauthorized.TokenExpiredException;
import gift.domain.exception.unauthorized.TokenStringInvalidException;
import gift.domain.exception.notFound.CategoryNotFoundException;
import gift.domain.exception.notFound.MemberNotFoundException;
import gift.domain.exception.notFound.NotFoundException;
import gift.domain.exception.notFound.OptionNotIncludedInProductOptionsException;
import gift.domain.exception.notFound.ProductNotFoundException;
import gift.domain.exception.notFound.ProductNotIncludedInWishlistException;
import gift.domain.exception.unauthorized.TokenNotFoundException;
import gift.domain.exception.notFound.OptionNotFoundException;
import gift.domain.exception.unauthorized.TokenUnexpectedErrorException;
import gift.domain.exception.unauthorized.UnauthorizedException;
import gift.global.apiResponse.ErrorApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 서버에서 발생하는 예외를 종합적으로 처리하는 클래스
 */
@RestControllerAdvice(basePackages = {"gift.domain", "gift.global"})
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        assert error != null;
        return ErrorApiResponse.of(
            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, error.getField()) + ": " + error.getDefaultMessage(),
            ErrorCode.FIELD_VALIDATION_FAIL.getErrorIdentifier(),
            ErrorCode.FIELD_VALIDATION_FAIL.getErrorCode(),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        ProductOptionsEmptyException.class,
        OptionQuantityOutOfRangeException.class,
        OptionUpdateActionInvalidException.class,
        OauthVendorIllegalException.class
    })
    public ResponseEntity<ErrorApiResponse> handleBadRequestException(BadRequestException e) {
        return ErrorApiResponse.of(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        TokenNotFoundException.class,
        TokenExpiredException.class,
        TokenStringInvalidException.class,
        TokenUnexpectedErrorException.class
    })
    public ResponseEntity<ErrorApiResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ErrorApiResponse.unauthorized(e);
    }

    @ExceptionHandler({
        MemberIncorrectLoginInfoException.class,
        MemberNotAdminException.class
    })
    public ResponseEntity<ErrorApiResponse> handleForbiddenException(ForbiddenException e) {
        return ErrorApiResponse.forbidden(e);
    }

    @ExceptionHandler({
        ProductNotFoundException.class,
        MemberNotFoundException.class,
        ProductNotIncludedInWishlistException.class,
        CategoryNotFoundException.class,
        OptionNotFoundException.class,
        OptionNotIncludedInProductOptionsException.class
    })
    public ResponseEntity<ErrorApiResponse> handleNotFoundException(NotFoundException e) {
        return ErrorApiResponse.notFound(e);
    }

    @ExceptionHandler({
        ProductAlreadyExistsException.class,
        MemberAlreadyExistsException.class,
        CategoryAlreadyExistsException.class,
        CategoryHasProductsException.class,
        OptionAlreadyExistsInProductException.class
    })
    public ResponseEntity<ErrorApiResponse> handleConflictException(ConflictException e) {
        return ErrorApiResponse.conflict(e);
    }
}
