package gift.dto;

public class MemberPointSubtractRequestDto {

    private Long amount;

    public MemberPointSubtractRequestDto(Long amount) {
        this.amount= amount;
    }

    public Long getAmount() {
        return amount;
    }

}
