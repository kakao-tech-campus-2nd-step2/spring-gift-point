package gift.service.member;

import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.mapper.MemberMapper;
import gift.web.dto.MemberDto;
import gift.web.exception.MemberNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public List<MemberDto> getMembers() {
        return memberRepository.findAll()
            .stream()
            .map(memberMapper::toDto)
            .toList();
    }

    public Member getMemberEntityByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("멤버가 엄슴다"));
    }

    public MemberDto getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .map(memberMapper::toDto)
            .orElseThrow(() -> new MemberNotFoundException("멤버가 엄슴다"));
    }

    @Transactional
    public void createMember(MemberDto memberDto) {
        memberRepository.save(memberMapper.toEntity(memberDto));
    }

    @Transactional
    public MemberDto updateMember(String email, MemberDto memberDto) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("멤버가 없슴다."));

        member.updateMember(memberDto.email(), memberDto.password());
        // 의문 : jpa의 변경감지로 인해서 위의 updateMember에서 이미 업데이트 될 것인데, save를 또 할 필요가 있을까 ?
        return memberMapper.toDto(member);
    }

    public void deleteMember(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("멤버가 없슴다."));
        memberRepository.delete(member);
    }
}