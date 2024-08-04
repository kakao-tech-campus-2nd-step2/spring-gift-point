package gift.service;

import static gift.util.constants.MemberConstants.EMAIL_ALREADY_USED;
import static gift.util.constants.MemberConstants.ID_NOT_FOUND;
import static gift.util.constants.MemberConstants.INVALID_CREDENTIALS;
import static gift.util.constants.MemberConstants.INVALID_REGISTER_TYPE;

import gift.dto.member.MemberAuthResponse;
import gift.dto.member.MemberEditRequest;
import gift.dto.member.MemberEditResponse;
import gift.dto.member.MemberLoginRequest;
import gift.dto.member.MemberOAuthResponse;
import gift.dto.member.MemberPointRequest;
import gift.dto.member.MemberPointResponse;
import gift.dto.member.MemberRegisterRequest;
import gift.exception.member.EmailAlreadyUsedException;
import gift.exception.member.ForbiddenException;
import gift.model.Member;
import gift.model.RegisterType;
import gift.repository.MemberRepository;
import gift.util.JWTUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JWTUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public MemberAuthResponse registerMember(MemberRegisterRequest memberRegisterRequest) {
        if (memberRepository.existsByEmail(memberRegisterRequest.email())) {
            throw new EmailAlreadyUsedException(EMAIL_ALREADY_USED);
        }

        Member member = new Member(
            memberRegisterRequest.email(),
            memberRegisterRequest.password(),
            RegisterType.DEFAULT
        );
        Member savedMember = memberRepository.save(member);
        String token = jwtUtil.generateToken(savedMember.getId(), member.getEmail());

        return new MemberAuthResponse(savedMember.getEmail(), token);
    }

    public MemberOAuthResponse registerKakaoMember(MemberRegisterRequest memberRegisterRequest) {
        if (memberRepository.existsByEmail(memberRegisterRequest.email())) {
            throw new EmailAlreadyUsedException(EMAIL_ALREADY_USED);
        }

        Member member = new Member(
            memberRegisterRequest.email(),
            memberRegisterRequest.password(),
            RegisterType.KAKAO
        );
        Member savedMember = memberRepository.save(member);
        String token = jwtUtil.generateToken(savedMember.getId(), member.getEmail());

        return new MemberOAuthResponse(
            savedMember.getId(),
            savedMember.getEmail(),
            token,
            savedMember.getRegisterType()
        );
    }

    public MemberAuthResponse loginMember(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByEmail(memberLoginRequest.email())
            .orElseThrow(() -> new ForbiddenException(INVALID_CREDENTIALS));

        if (!member.isRegisterTypeDefault()) {
            throw new ForbiddenException(INVALID_REGISTER_TYPE);
        }

        if (!member.isPasswordMatching(memberLoginRequest.password())) {
            throw new ForbiddenException(INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(member.getId(), member.getEmail());
        return new MemberAuthResponse(member.getEmail(), token);
    }

    public MemberOAuthResponse loginKakaoMember(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new ForbiddenException(INVALID_CREDENTIALS));

        if (!member.isRegisterTypeKakao()) {
            throw new ForbiddenException(INVALID_REGISTER_TYPE);
        }

        String token = jwtUtil.generateToken(member.getId(), member.getEmail());
        return new MemberOAuthResponse(member.getId(), member.getEmail(), token, member.getRegisterType());
    }

    @Transactional(readOnly = true)
    public List<MemberEditResponse> getAllMembers() {
        return memberRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberEditResponse getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .map(this::convertToDTO)
            .orElseThrow(() -> new ForbiddenException(ID_NOT_FOUND + memberId));
    }

    public MemberEditResponse updateMember(Long memberId, MemberEditRequest memberEditRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ForbiddenException(ID_NOT_FOUND + memberId));

        boolean emailChanged = !member.isEmailMatching(memberEditRequest.email());
        boolean emailAlreadyUsed = memberRepository.existsByEmail(memberEditRequest.email());

        if (emailChanged && emailAlreadyUsed) {
            throw new EmailAlreadyUsedException(EMAIL_ALREADY_USED);
        }

        String changedPassword = memberEditRequest.password();
        if (member.isRegisterTypeKakao()) {
            changedPassword = member.getPassword();
        }

        member.update(memberEditRequest.email(), changedPassword);
        Member updatedMember = memberRepository.save(member);
        return convertToDTO(updatedMember);
    }

    public void deleteMember(Long memberId) throws ForbiddenException {
        if (!memberRepository.existsById(memberId)) {
            throw new ForbiddenException(ID_NOT_FOUND + memberId);
        }
        memberRepository.deleteById(memberId);
    }

    @Transactional(readOnly = true)
    public MemberPointResponse getPoints(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ForbiddenException(ID_NOT_FOUND + memberId));
        return new MemberPointResponse(member.getPoints());
    }

    public MemberPointResponse addPoints(Long memberId, MemberPointRequest memberPointRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ForbiddenException(ID_NOT_FOUND + memberId));
        member.addPoints(memberPointRequest.amount());
        memberRepository.save(member);
        return new MemberPointResponse(member.getPoints());
    }

    public void deductPoints(Long memberId, MemberPointRequest memberPointRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ForbiddenException(ID_NOT_FOUND + memberId));
        member.deductPoints(memberPointRequest.amount());
        memberRepository.save(member);
    }

    // Mapper methods
    public Member convertToEntity(MemberEditResponse memberEditResponse) {
        return new Member(memberEditResponse.id(), memberEditResponse.email(), null, memberEditResponse.registerType());
    }

    private MemberEditResponse convertToDTO(Member member) {
        return new MemberEditResponse(member.getId(), member.getEmail(), null, member.getRegisterType());
    }
}
