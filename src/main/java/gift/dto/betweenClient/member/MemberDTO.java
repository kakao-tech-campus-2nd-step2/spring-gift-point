package gift.dto.betweenClient.member;

import gift.constants.MemberContants;
import gift.entity.Member;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public class MemberDTO {


    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Hidden
    @Null(message = "계정 타입은 입력되지 말아야 합니다.")
    private String accountType;

    @Hidden
    private String name;

    @Hidden
    private String role;

    public MemberDTO() {
        this.name = MemberContants.DEFAULT_USER_NAME;
        this.role = MemberContants.DEFAULT_USER_ROLE;
    }

    public MemberDTO(String email, String password, String accountType) {
        this(email, password, accountType, MemberContants.DEFAULT_USER_NAME, MemberContants.DEFAULT_USER_ROLE);
    }

    public MemberDTO(String email, String password, String accountType, String name) {
        this(email, password, accountType, name, MemberContants.DEFAULT_USER_ROLE);
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

    public Member convertToMember(Long point) {
        return new Member(this.email, this.password, this.accountType, this.name, this.role, point);
    }

    public static MemberDTO convertToMemberDTO(Member member) {
        return new MemberDTO(member.getEmail(), member.getPassword(), member.getAccountType(), member.getName(),
                member.getRole());
    }
}