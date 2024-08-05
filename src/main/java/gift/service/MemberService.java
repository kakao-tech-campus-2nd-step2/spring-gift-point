package gift.service;

import gift.dto.MemberPointChargingRequestDto;
import gift.dto.MemberPointSubtractRequestDto;
import gift.dto.MemberPointViewResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository, BasicTokenService basicTokenService) {
        this.memberRepository = memberRepository;
    }

    public MemberResponseDto save(String email, String password) {

        Member newMember = new Member(email, password);

        Member actualMember = memberRepository.save(newMember);
        String token = generateTokenFrom(actualMember.getEmail());
        return new MemberResponseDto(actualMember, token);
    }


    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    public List<MemberResponseDto> getAllMemberResponseDto() {
        return getAll().stream().map(this::fromEntity2).toList();
    }

    public String generateTokenFrom(String userEmail) {
        return findUserIdFrom(userEmail).toString();
    }

    private Long findUserIdFrom(String userEmail) {
        return memberRepository.findByEmail(userEmail).getId();
    }

    public boolean login(String email, String password) throws AuthenticationException {
        MemberRequestDto dbUserDto = fromEntity(memberRepository.findByEmail(email));

        return validatePassword(password, dbUserDto.getPassword());
    }

    public MemberRequestDto fromEntity(Member member) {
        return new MemberRequestDto(member.getEmail());
    }

    public MemberResponseDto fromEntity2(Member member) {
        return new MemberResponseDto(member.getId(), member.getEmail());
    }

    private boolean validatePassword(String inputPassword, String dbUserPassword) throws AuthenticationException {

        if (inputPassword.equals(dbUserPassword)) {
            return true;
        }
        throw new AuthenticationException("Invalid password");
    }

    public Member getById(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public MemberPointViewResponseDto updateMemberPoint(MemberPointChargingRequestDto memberPointChargingRequestDto, Long memberId) {

        Long addPoint = memberPointChargingRequestDto.getAmount();
        Member oldMember = memberRepository.findById(memberId).get();

        Long newPoint = oldMember.getPoint() + addPoint;

        Member newMember = new Member(oldMember.getId(),oldMember.getEmail(),oldMember.getToken(), newPoint);
        Member savedMember = memberRepository.save(newMember);

        return new MemberPointViewResponseDto(savedMember.getPoint());
    }

    public MemberPointViewResponseDto updateMemberPoint(MemberPointSubtractRequestDto memberPointSubtractRequestDto, Long memberId) {

        Long subtractPoint = memberPointSubtractRequestDto.getAmount();
        Member oldMember = memberRepository.findById(memberId).get();

        Long newPoint = oldMember.getPoint() - subtractPoint;

        Member newMember = new Member(oldMember.getId(),oldMember.getEmail(),oldMember.getToken(), newPoint);
        Member savedMember = memberRepository.save(newMember);

        return new MemberPointViewResponseDto(savedMember.getPoint());
    }

    public MemberPointViewResponseDto getMemberPoint(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        return new MemberPointViewResponseDto(member.getPoint());
    }
}
