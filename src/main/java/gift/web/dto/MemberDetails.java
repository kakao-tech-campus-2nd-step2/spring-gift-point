package gift.web.dto;

import gift.domain.Member;
import gift.domain.constants.Platform;
import gift.domain.vo.Email;

public class MemberDetails {

    private Long id;
    private Email email;
    private Platform platform;

    public MemberDetails(Long id, Email email, Platform platform) {
        this.id = id;
        this.email = email;
        this.platform = platform;
    }

    public static MemberDetails from(Member member) {
        return new MemberDetails(member.getId(), member.getEmail(), member.getPlatform());
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public String getEmailValue() {
        return email.getValue();
    }

    public Platform getPlatform() {
        return platform;
    }
}
