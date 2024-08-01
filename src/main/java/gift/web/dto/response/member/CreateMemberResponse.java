package gift.web.dto.response.member;

import gift.authentication.token.Token;
import gift.domain.Member;

public class CreateMemberResponse {

    private Long id;

    private String email;

    private String name;

    private String token;

    public CreateMemberResponse(Long id, String email, String name, String token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public static CreateMemberResponse of(Member member, Token token) {
        return new CreateMemberResponse(member.getId(), member.getEmail().getValue(), member.getName(), token.getValue());
    }
}
