package gift.global.response;

import gift.global.exception.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 응답 객체를 생성하는 클래스
 */
public class ResponseMaker {

    /**
     * BODY 에 성공 메시지와 데이터를 보냄
     */
    public static <T> ResponseEntity<ResultResponseDto<T>> createResponse(HttpStatus status, T data) {
        ResultResponseDto<T> resultResponseDto = new ResultResponseDto<>(data);

        return ResponseEntity.status(status).body(resultResponseDto);
    }

    /**
     * BODY 에 성공 메시지만 보냄 (데이터 X)
     */
    public static ResponseEntity createSimpleResponse(HttpStatus status) {
        return ResponseEntity.status(status).build();
    }

    /**
     * BODY 에 에러 메시지만 보냄 (데이터 X)
     */
    public static ResponseEntity<ErrorResponseDto> createErrorResponse(ErrorCode errorCode,
        String message) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(message);

        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(errorResponseDto);
    }

    /**
     * 헤더에 Jwt 담아서 반환
     */
    public static ResponseEntity<SimpleResultResponseDto> createSimpleResponseWithJwtOnHeader(
        HttpStatus status, String message, String jwt) {
        SimpleResultResponseDto simpleResultResponseDto = new SimpleResultResponseDto(message);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        return ResponseEntity
            .status(status)
            .headers(headers).body(simpleResultResponseDto);
    }
}
