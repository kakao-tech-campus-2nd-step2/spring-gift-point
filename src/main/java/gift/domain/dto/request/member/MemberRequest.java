package gift.domain.dto.request.member;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import gift.domain.entity.Member;
import gift.global.WebConfig.Constants.Domain;
import gift.global.WebConfig.Constants.Domain.Member.Permission;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 파생된 멤버 요청을 처리할 수 있게 함
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "user-type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = LocalMemberRequest.class, name = "local"),
    @JsonSubTypes.Type(value = KakaoOauthMemberRequest.class, name = "kakao"),
})
public abstract class MemberRequest {

    @NotNull
    protected final Domain.Member.Type userType;
    @NotNull
    protected final String email;

    private static final Logger log = LoggerFactory.getLogger(MemberRequest.class);

    protected MemberRequest(Type userType, String email) {
        this.userType = userType;
        this.email = email;
    }

    public Domain.Member.Type getUserType() {
        return userType;
    }

    public String getEmail() {
        return email;
    }

    public Member toEntity(Permission permission) {
        return new Member(email, permission, userType);
    }
}
