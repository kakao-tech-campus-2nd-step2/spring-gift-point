package gift.member;

import static gift.exception.ErrorMessage.MEMBER_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.MEMBER_NOT_FOUND;
import static gift.exception.ErrorMessage.WRONG_PASSWORD;

import gift.exception.FailedLoginException;
import gift.member.dto.MemberChargePointRequestDTO;
import gift.member.dto.MemberPointResponseDTO;
import gift.member.dto.MemberRequestDTO;
import gift.member.dto.MemberResponseDTO;
import gift.member.entity.Member;
import gift.token.JwtProvider;
import gift.token.MemberTokenDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    public final MemberRepository memberRepository;
    public final JwtProvider jwtProvider;

    public MemberService(
        MemberRepository memberRepository,
        JwtProvider jwtProvider
    ) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public Member getMember(String email) {
        return memberRepository.findById(email)
            .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND));
    }

    public String register(MemberRequestDTO memberRequestDTO) {
        memberRepository.findById(memberRequestDTO.getEmail())
            .ifPresentOrElse(
                e -> {
                    throw new IllegalArgumentException(MEMBER_ALREADY_EXISTS);
                },
                () -> memberRepository.save(
                    new Member(memberRequestDTO.getEmail(), memberRequestDTO.getPassword())
                )
            );

        return jwtProvider.generateToken(
            new MemberTokenDTO(memberRequestDTO.getEmail())
        );
    }

    public void registerIfNotExists(String email, String password) {
        if (!memberRepository.existsById(email)) {
            memberRepository.save(new Member(email, password));
        }
    }

    @Transactional(readOnly = true)
    public String login(MemberRequestDTO memberRequestDTO) {
        Member findMember = memberRepository.findById(memberRequestDTO.getEmail())
            .orElseThrow(() -> new FailedLoginException(MEMBER_NOT_FOUND));

        verifyPassword(findMember, memberRequestDTO);

        return jwtProvider.generateToken(
            new MemberTokenDTO(memberRequestDTO.getEmail())
        );
    }

    private void verifyPassword(Member member, MemberRequestDTO memberRequestDTO) {
        if (!member.isSamePassword(
            new Member(memberRequestDTO.getEmail(), memberRequestDTO.getPassword()))) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }
    }

    public void chargePoint(MemberChargePointRequestDTO memberChargePointRequestDTO) {
        getMember(memberChargePointRequestDTO.getEmail())
            .chargePoint(memberChargePointRequestDTO.getPoint());
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDTO> getAllMembers() {
        return memberRepository.findAll()
            .stream()
            .map(member -> new MemberResponseDTO(
                member.getEmail(),
                member.getPoint()
            )).toList();
    }

    @Transactional(readOnly = true)
    public MemberPointResponseDTO getPoint(String token) {
        Member member = getMember(
            jwtProvider.getMemberTokenDTOFromToken(token).getEmail()
        );

        return new MemberPointResponseDTO(member.getPoint());
    }
}
