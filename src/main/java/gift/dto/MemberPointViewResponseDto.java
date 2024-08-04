package gift.dto;

public class MemberPointViewResponseDto {
    private Long amount;

    public MemberPointViewResponseDto(Long remainPointAmount) {
        this.amount = remainPointAmount;
    }
}
