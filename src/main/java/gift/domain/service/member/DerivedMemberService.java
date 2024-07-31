package gift.domain.service.member;

import gift.domain.dto.request.member.MemberRequest;
import gift.domain.dto.response.MemberResponse;
import gift.domain.entity.Member;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

public interface DerivedMemberService<DerivedMember, DerivedMemberRequest extends MemberRequest> {

    Member registerMember(MemberRequest requestDto, Member member);
    Member loginMember(MemberRequest requestDto, Member member);
    DerivedMember findDerivedMemberBy(Member member);
    DerivedMemberRequest convert(MemberRequest requestDto);

    @Component
    class Factory {

        private final Map<Type, DerivedMemberService<?,?>> map;

        public Factory(LocalMemberService localMemberService, KakaoOauthMemberService kakaoOauthMemberService) {
            this.map = new HashMap<>();
            map.put(Type.LOCAL, localMemberService);
            map.put(Type.KAKAO, kakaoOauthMemberService);
        }

        public DerivedMemberService<?,?> getInstance(MemberRequest request) {
            return map.get(request.getUserType());
        }
    }
}
