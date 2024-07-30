package gift.global.utils;

import gift.global.response.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseHelper {
    private ResponseHelper() {
    }

    public static <T> ResponseEntity<ResultResponseDto<T>> createResponse(ResultCode resultCode, T data) {
        ResultResponseDto<T> resultResponseDto = new ResultResponseDto<>(resultCode, data);
        return org.springframework.http.ResponseEntity.status(resultCode.getStatus())
                .body(resultResponseDto);
    }

    public static ResponseEntity<SimpleResultResponseDto> createSimpleResponse(ResultCode resultCode) {
        var resultResponseDto = new SimpleResultResponseDto(resultCode);
        return ResponseEntity.status(resultCode.getStatus())
                .body(resultResponseDto);
    }


    public static ResponseEntity<SimpleResultResponseDto> createSimpleResponse(ResultCode resultCode, HttpHeaders headers) {
        var resultResponseDto = new SimpleResultResponseDto(resultCode);

        return ResponseEntity.status(resultCode.getStatus())
                .headers(headers)
                .body(resultResponseDto);
    }

    public static ResponseEntity<ErrorResponseDto> createErrorResponse(ErrorCode errorCode) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);
        return ResponseEntity.status(errorCode.getStatus())
                .body(errorResponseDto);
    }
}
