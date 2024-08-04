package gift.ServiceTest.FakeRepository;

import gift.Model.Entity.Member;

import java.util.*;

public class FakeMemberRepository {
    private final Map<Long, Member> memberMap = new HashMap<>();
    private Long id = 1L;

    public Member save(Member member) {
        memberMap.put(id++, member);
        return member;
    }

    public List<Member> findAll() {
        return new ArrayList<>(memberMap.values());
    }

    public Optional<Member> findByEmail(String email) {
        return memberMap.values()
                .stream()
                .filter(it -> it.getEmail().getValue().equals(email))
                .findFirst();
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(memberMap.get(id));
    }

    public void deleteById(Long id) {
        memberMap.remove(id);
    }
}
