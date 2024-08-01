package gift.service;

import gift.model.Member;
import java.util.Optional;

public interface MemberService {
    Optional<String> registerMember(Member member);
    Optional<String> login(String email, String password);
    Optional<Member> findById(Long id);
}
