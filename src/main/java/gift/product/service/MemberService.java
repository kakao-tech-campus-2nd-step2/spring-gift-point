package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.dto.MemberDTO;
import gift.product.dto.PointRequestDTO;
import gift.product.dto.PointResponseDTO;
import gift.product.dto.TokenDTO;
import gift.product.exception.InvalidIdException;
import gift.product.model.Member;
import gift.product.repository.MemberRepository;
import gift.product.util.JwtUtil;
import gift.product.validation.MemberValidation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final MemberValidation memberValidation;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public MemberService(
            MemberRepository memberRepository,
            JwtUtil jwtUtil,
            MemberValidation memberValidation,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.memberValidation = memberValidation;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDTO signUp(MemberDTO memberDTO) {
        System.out.println("[MemberService] signUp()");
        memberValidation.signUpValidation(memberDTO.getEmail());
        Member member = memberRepository.save(memberDTO.convertToDomain(passwordEncoder));
        return new TokenDTO(jwtUtil.generateToken(member.getEmail()));
    }

    public TokenDTO login(MemberDTO memberDTO) {
        System.out.println("[MemberService] login()");
        memberValidation.loginValidation(memberDTO);
        return new TokenDTO(jwtUtil.generateToken(memberDTO.getEmail()));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public void addPoint(PointRequestDTO pointRequestDTO) {
        System.out.println("[MemberService] addPoint()");
        Member member = memberRepository.findById(pointRequestDTO.getMemberId())
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        member.addPoint(pointRequestDTO.getPoint());
        memberRepository.save(member);
    }

    public PointResponseDTO getPoint(String authorization) {
        Member member = jwtUtil.parsingToken(authorization);
        return new PointResponseDTO(
            memberRepository.findById(member.getId())
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID))
            .getPoint());
    }
}
