package gift.dto;

import gift.model.Member;

public class MemberDTO {
    private Long id;
    private String email;
    private String password;

    public MemberDTO(Long id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static MemberDTO getMemberDTO(Member member){
        return new MemberDTO(member.getId(), member.getEmail(), member.getPassword());
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
