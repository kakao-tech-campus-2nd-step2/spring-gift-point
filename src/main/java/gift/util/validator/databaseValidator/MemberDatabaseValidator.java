package gift.util.validator.databaseValidator;

import gift.dto.betweenClient.member.MemberDTO;
import gift.entity.Member;
import gift.exception.BadRequestExceptions.UserNotFoundException;
import gift.repository.MemberRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberDatabaseValidator {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberDatabaseValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public Member validateMember(MemberDTO memberDTO) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberDTO.getEmail());
        if (optionalMember.isEmpty()) {
            throw new UserNotFoundException("유저를 찾을 수 없습니다.");
        }

        return optionalMember.get();
    }
}