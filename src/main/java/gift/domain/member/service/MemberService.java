package gift.domain.member.service;

import gift.domain.member.dto.MemberRequest;
import gift.domain.member.dto.MemberResponse;
import gift.domain.member.entity.Member;
import gift.domain.member.exception.MemberNotFoundException;
import gift.domain.member.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public Page<MemberResponse> getAllMember(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return memberRepository.findAll(pageable).map(this::entityToDto);
    }

    @Transactional
    public String register(MemberRequest memberRequest) {

        Member member = memberRepository.save(dtoToEntity(memberRequest));

        return jwtUtil.generateToken(member);
    }

    public String login(MemberRequest memberRequest) {
        Member member = memberRepository
            .findByEmail(memberRequest.getEmail())
            .orElseThrow(() -> new MemberNotFoundException("유저가 존재하지 않습니다."));

        if (member.getPassword().equals(memberRequest.getPassword())) {
            return jwtUtil.generateToken(member);
        }
        return null;
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository
            .findById(id)
            .orElseThrow(() -> new MemberNotFoundException("삭제하려는 유저가 존재하지 않습니다."));
        memberRepository.delete(member);
    }

    public Member getMemberFromToken(String token) {
        String email = jwtUtil.getEmailFromToken(token);

        if (email != null) {
            return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("유저가 존재하지 않습니다.")
                );
        }
        return null;
    }

    private MemberResponse entityToDto(Member member) {
        return new MemberResponse(member.getEmail(),
            member.getPassword());
    }

    private Member dtoToEntity(MemberRequest memberRequest) {
        return new Member(memberRequest.getEmail(), memberRequest.getPassword());
    }
}
