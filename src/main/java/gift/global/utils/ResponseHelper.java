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


    public static ResponseEntity<SimpleResultResponseDto> createSimpleResponse(ResultCode resultCode, Map<String, String> headers) {
        var resultResponseDto = new SimpleResultResponseDto(resultCode);
        HttpHeaders httpHeaders = new HttpHeaders();

        // 입력받은 헤더를 HttpHeaders 객체에 추가
        for (Map.Entry<String, String> header : headers.entrySet()) {
            httpHeaders.add(header.getKey(), header.getValue());
        }

        return ResponseEntity.status(resultCode.getStatus())
                .headers(httpHeaders)
                .body(resultResponseDto);
    }

    public static ResponseEntity<ErrorResponseDto> createErrorResponse(ErrorCode errorCode) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);
        return ResponseEntity.status(errorCode.getStatus())
                .body(errorResponseDto);
    }
}
