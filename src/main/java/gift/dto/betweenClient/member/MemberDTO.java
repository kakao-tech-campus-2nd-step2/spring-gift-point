package gift.dto.betweenClient.member;

import gift.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public class MemberDTO {


    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Null(message = "계정 타입은 입력되지 말아야 합니다.")
    private String accountType;

    private String name;

    private String role;

    public MemberDTO() {
        this.name = "default_user";
        this.role = "USER";
    }

    public MemberDTO(String email, String password, String accountType) {
        this(email, password, accountType, "default_user", "USER");
    }

    public MemberDTO(String email, String password, String accountType, String name) {
        this(email, password, accountType, name, "USER");
    }

    public MemberDTO(String email, String password, String accountType, String name, String role) {
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.name = name;
        this.role = role;
    }

    public String getEmail() { return email; }
    public String getPassword() {
        return password;
    }
    public String getAccountType() { return accountType; }
    public String getName() {
        return name;
    }
    public String getRole() {
        return role;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Member convertToMember() {
        return new Member(this.email, this.password, this.accountType, this.name, this.role);
    }

    public static MemberDTO convertToMemberDTO(Member member) {
        return new MemberDTO(member.getEmail(), member.getPassword(), member.getAccountType(), member.getName(),
                member.getRole());
    }
}