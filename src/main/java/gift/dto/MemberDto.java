package gift.dto;

import gift.entity.Member;


public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private Role role;

    // 기본 생성자
    public MemberDto() {
    }

    // 모든 필드를 받는 생성자
    public MemberDto(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    // Member 엔티티를 받아서 Dto로 변환하는 생성자
    public MemberDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.role = member.getRole();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
