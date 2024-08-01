package gift.DTO.member;

import gift.domain.Member;

public class MemberResponse {

    private Long id;
    private String email;
    private Long kakaoId;
    private Boolean isKakaoMember;

    public MemberResponse() {
    }

    public MemberResponse(Long id, String email, Long kakaoId, Boolean isKakaoMember) {
        this.id = id;
        this.email = email;
        this.kakaoId = kakaoId;
        this.isKakaoMember = isKakaoMember;
    }

    public static MemberResponse fromEntity(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getEmail(),
            member.getKakaoId(),
            member.isKakaoMember()
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getKakaoId() {
        return kakaoId;
    }

    public Boolean getKakaoMember() {
        return isKakaoMember;
    }
}
