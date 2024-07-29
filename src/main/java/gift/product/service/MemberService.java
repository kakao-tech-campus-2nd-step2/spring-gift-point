package gift.product.service;

import gift.product.dto.MemberDTO;
import gift.product.dto.TokenDTO;
import gift.product.model.Member;
import gift.product.repository.MemberRepository;
import gift.product.util.JwtUtil;
import gift.product.validation.MemberValidation;
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
}
