package gift.domain;

import gift.domain.Product.ProductResponse;
import gift.entity.MemberEntity;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.stream.Collectors;

public class Member {

    public record MemberRequest(
        @NotNull(message = "email을 작성해주세요")
        @Email
        String email,
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자 사이로 작성해주세요")
        String password
    ) {

    }

    public record MemberResponse(
        Long id,
        String email
    ){
        public static MemberResponse from(MemberEntity memberEntity) {
            return new MemberResponse(
                memberEntity.getId(),
                memberEntity.getEmail()
            );
        }
    }

}
