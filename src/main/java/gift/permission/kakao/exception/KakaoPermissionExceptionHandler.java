package gift.permission.kakao.exception;

import static gift.global.dto.ApiResponseDto.FAILURE;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.dto.ApiResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;


// kakao api와 연동 중 발생한 모든 에러 처리
@RestControllerAdvice(basePackages = "gift.permission.kakao")
public class KakaoPermissionExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ApiResponseDto<Void> handler(HttpClientErrorException httpClientErrorException) {
        var response = httpClientErrorException.getResponseBodyAsString();
        var objectMapper = new ObjectMapper();

        try {
            var jsonNode = objectMapper.readTree(response);
            var errorCode = jsonNode.path("error_code").asText();
            return FAILURE(getMessageFromErrorCode(errorCode));
        } catch (Exception e) {
            // kakao에서 반환하는 에러 메시지에 error_code가 없으면 알 수 없는 에러
            return FAILURE("알 수 없는 에러입니다.");
        }
    }

    private String getMessageFromErrorCode(String errorCode) {
        if (errorCode.equals("KOE203")) {
            return "필수 동의항목에 동의해주세요.";
        }

        if (errorCode.equals("KOE204")) {
            return "임의로 파리미터를 추가할 수 없습니다.";
        }

        if (errorCode.equals("KOE205")) {
            return "임의로 동의항목을 추가할 수 없습니다.";
        }

        if (errorCode.equals("KOE207")) {
            return "파라미터가 누락되었습니다.";
        }

        if (errorCode.equals("KOE009")) {
            return "다른 플랫폼을 이용해주세요.";
        }

        if (errorCode.equals("KOE320")) {
            return "만료된 인가코드입니다.";
        }

        // 예상한 어떤 에러도 아닌 경우는 에러 코드를 반환
        return errorCode;
    }
}
