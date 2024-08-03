package gift.global.dto;

// 반환이 없는 api의 반환은 이 dto를 반환합니다.
public record ApiResponseDto<T>(
    boolean success,
    T data,
    String errorMsg
) {

    // 성공하고 아무 것도 반환 안 하는 경우
    public static ApiResponseDto<Void> SUCCESS() {
        return new ApiResponseDto<>(true, null, null);
    }

    // 실패한 경우 (반환값 없음)
    public static ApiResponseDto<Void> FAILURE(String errorMsg) {
        return new ApiResponseDto<>(false, null, errorMsg);
    }

    // 성공하고 데이터를 넘겨주는 경우
    public static <T> ApiResponseDto<T> SUCCESS(T data) {
        return new ApiResponseDto<>(true, data, null);
    }
}
