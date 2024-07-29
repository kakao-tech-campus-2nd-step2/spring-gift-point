package gift.dto;

import gift.entity.Member;

public class MemberResponseDto {

    private Long id;
    private final String email;
    private final String password;

    private String token;

    public MemberResponseDto(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public MemberResponseDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public MemberResponseDto(Member actualMember, String token) {
        this.id = actualMember.getId();
        this.email = actualMember.getEmail();
        this.password = actualMember.getPassword();
        this.token = actualMember.getToken();
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

}
