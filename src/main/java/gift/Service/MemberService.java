package gift.Service;

import gift.Entity.Member;
import gift.Mapper.Mapper;
import gift.Model.MemberDto;
import gift.Repository.MemberJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;
    private final Mapper mapper;

    @Autowired
    public MemberService(MemberJpaRepository memberJpaRepository, Mapper mapper) {
        this.memberJpaRepository = memberJpaRepository;
        this.mapper = mapper;
    }

    public void register(MemberDto memberDto) {
        Member member = mapper.memberDtoToEntity(memberDto);
        member.setKakaoId(memberDto.getKakaoId());
        memberJpaRepository.save(member);
    }

    public Optional<MemberDto> findByMemberId(long id) {
        return memberJpaRepository.findById(id)
                .map(mapper::memberToDto);
    }

    public Optional<MemberDto> findByEmail(String email) {
        Optional<Member> member = memberJpaRepository.findByEmail(email);
        return member.map(mapper::memberToDto);
    }

    public boolean isAdmin(String email) {
        Optional<Member> user = memberJpaRepository.findByEmail(email);
        return user.get().isAdmin();
    }

    public boolean authenticate(String email, String password) {
        Optional<Member> user = memberJpaRepository.findByEmailAndPassword(email, password);
        return user != null && user.get().getPassword().equals(password);
    }

    public Optional<MemberDto> findByKakaoId(long kakaoId) {
        Optional<Member> member = memberJpaRepository.findByKakaoId(kakaoId);
        return member.map(mapper::memberToDto);
    }

    public void updatePoint(MemberDto memberDto) {
        Member member = memberJpaRepository.findById(memberDto.getId()).orElseThrow();
        member.setPoint(memberDto.getPoint());
        memberJpaRepository.save(member);
    }
}
