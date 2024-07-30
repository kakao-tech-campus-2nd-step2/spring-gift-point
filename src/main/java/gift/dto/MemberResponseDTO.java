package gift.dto;

import gift.model.Member;

public class MemberResponseDTO {
    private Long id;
    private String email;

    public MemberResponseDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public static MemberResponseDTO fromEntity(Member member) {
        return new MemberResponseDTO(member.getId(), member.getEmail());
    }
}