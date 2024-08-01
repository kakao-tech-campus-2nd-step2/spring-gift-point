package gift.web.dto.response.member;

import gift.domain.Member;

public class CreateMemberResponse {

    private Long id;

    private String email;

    private String name;

    public CreateMemberResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public static CreateMemberResponse fromEntity(Member member) {
        return new CreateMemberResponse(member.getId(), member.getEmail().getValue(), member.getName());
    }
}
