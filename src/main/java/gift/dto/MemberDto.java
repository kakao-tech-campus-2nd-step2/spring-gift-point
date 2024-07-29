package gift.dto;

import gift.domain.member.Member;

public class MemberDto {

    private String name;
    private String email;
    private String password;

    public MemberDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Member toEntity() {
        return new Member.MemberBuilder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .build();
    }

}
