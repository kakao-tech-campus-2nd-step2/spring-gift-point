package gift.mapper;

import gift.DTO.MemberDTO;
import gift.entity.MemberEntity;
import org.springframework.stereotype.Component;

/**
 * MemberMapper 클래스는 MemberEntity와 MemberDTO 간의 변환을 담당합니다.
 */
@Component
public class MemberMapper {

    /**
     * MemberEntity를 MemberDTO로 변환하는 메서드
     *
     * @param memberEntity 변환할 MemberEntity 객체
     * @return 변환된 MemberDTO 객체
     */
    public MemberDTO toMemberDTO(MemberEntity memberEntity) {
        return new MemberDTO(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword()
        );
    }

    /**
     * MemberDTO를 MemberEntity로 변환하는 메서드
     *
     * @param memberDTO  변환할 MemberDTO 객체
     * @param idRequired ID 필요 여부
     * @return 변환된 MemberEntity 객체
     */
    public MemberEntity toMemberEntity(MemberDTO memberDTO, boolean idRequired) {
        var memberEntity = new MemberEntity();
        if (idRequired) {
            memberEntity.setId(memberDTO.getId());
        }
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setPassword(memberDTO.getPassword());
        return memberEntity;
    }

    /**
     * MemberDTO를 MemberEntity로 변환하는 메서드
     *
     * @param memberDTO 변환할 MemberDTO 객체
     * @return 변환된 MemberEntity 객체
     */
    public MemberEntity toMemberEntity(MemberDTO memberDTO) {
        return toMemberEntity(memberDTO, true);
    }
}
