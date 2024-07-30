package gift.product.validation;

import static gift.product.exception.GlobalExceptionHandler.DUPLICATE_EMAIL;
import static gift.product.exception.GlobalExceptionHandler.INVALID_INPUT;

import gift.product.dto.MemberDTO;
import gift.product.exception.DuplicateException;
import gift.product.exception.LoginFailedException;
import gift.product.model.Member;
import gift.product.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberValidation {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberValidation(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUpValidation(String email) {
        if(memberRepository.findByEmail(email).isPresent())
            throw new DuplicateException(DUPLICATE_EMAIL);
    }

    public void loginValidation(MemberDTO memberDTO) {
        Member member = memberRepository.findByEmail(memberDTO.getEmail())
                .orElseThrow(() -> new LoginFailedException(INVALID_INPUT));
        if(!passwordEncoder.matches(memberDTO.getPassword(), member.getPassword()))
            throw new LoginFailedException(INVALID_INPUT);
    }
}
