package gift.service;

import static gift.util.constants.MemberConstants.EMAIL_ALREADY_USED;
import static gift.util.constants.MemberConstants.ID_NOT_FOUND;
import static gift.util.constants.MemberConstants.INVALID_CREDENTIALS;

import gift.dto.member.MemberEditRequest;
import gift.dto.member.MemberLoginRequest;
import gift.dto.member.MemberRegisterRequest;
import gift.dto.member.MemberResponse;
import gift.exception.member.EmailAlreadyUsedException;
import gift.exception.member.ForbiddenException;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JWTUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JWTUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public MemberResponse registerMember(MemberRegisterRequest memberRegisterRequest) {
        if (memberRepository.existsByEmail(memberRegisterRequest.email())) {
            throw new EmailAlreadyUsedException(EMAIL_ALREADY_USED);
        }

        Member member = new Member(memberRegisterRequest.email(), memberRegisterRequest.password());
        Member savedMember = memberRepository.save(member);

        String token = jwtUtil.generateToken(savedMember.getId(), member.getEmail());
        return new MemberResponse(savedMember.getId(), savedMember.getEmail(), token);
    }

    public MemberResponse loginMember(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByEmail(memberLoginRequest.email())
            .orElseThrow(() -> new ForbiddenException(INVALID_CREDENTIALS));

        if (!member.isPasswordMatching(memberLoginRequest.password())) {
            throw new ForbiddenException(INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(member.getId(), member.getEmail());
        return new MemberResponse(member.getId(), member.getEmail(), token);
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public MemberResponse getMemberById(Long id) {
        return memberRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new ForbiddenException(INVALID_CREDENTIALS));
    }

    public MemberResponse updateMember(Long id, MemberEditRequest memberEditRequest) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new ForbiddenException(INVALID_CREDENTIALS));

        boolean emailChanged = !member.isEmailMatching(memberEditRequest.email());
        boolean emailAlreadyUsed = memberRepository.existsByEmail(memberEditRequest.email());

        if (emailChanged && emailAlreadyUsed) {
            throw new EmailAlreadyUsedException(EMAIL_ALREADY_USED);
        }

        member.update(memberEditRequest.email(), memberEditRequest.password());
        Member updatedMember = memberRepository.save(member);
        return convertToDTO(updatedMember);
    }

    public void deleteMember(Long id) throws ForbiddenException {
        if (!memberRepository.existsById(id)) {
            throw new ForbiddenException(ID_NOT_FOUND);
        }
        memberRepository.deleteById(id);
    }

    // Mapper methods
    public Member convertToEntity(MemberResponse memberResponse) {
        return new Member(memberResponse.id(), memberResponse.email(), null);
    }

    private MemberResponse convertToDTO(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), null);
    }
}
