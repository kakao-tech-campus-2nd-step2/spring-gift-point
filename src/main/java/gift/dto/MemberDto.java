package gift.dto;

import gift.domain.member.Member;

public class MemberDto {

    private String email;
    private String password;

    public MemberDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Member toEntity() {
        return new Member.MemberBuilder()
                .email(this.email)
                .password(this.password)
                .build();
    }

}
