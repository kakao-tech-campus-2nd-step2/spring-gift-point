package gift.repository;

import gift.authentication.token.TokenContext;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class MemberIdAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return TokenContext.getCurrentMemberId();
    }
}
