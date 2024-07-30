package gift.global.response;

public record ResultResponseDto<T>(String code, String message, T data) {
    // 자바 record 에서 생성자를 만들 경우, 반드시 canonical(표준) 생성자를 사용해야 한다!!
    public ResultResponseDto(ResultCode resultCode, T data) {
        this(resultCode.getCode(), resultCode.getMessage(), data);
    }
}
