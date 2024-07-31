package gift.web.dto.response.member;

import gift.domain.Member;

public class ReadMemberResponse {

    private Long id;
    private String email;
    private String password;
    private String name;

    public ReadMemberResponse(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static ReadMemberResponse fromEntity(Member member) {
        return new ReadMemberResponse(member.getId(), member.getEmail().getValue(), member.getPassword().getValue(),
            member.getName());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
