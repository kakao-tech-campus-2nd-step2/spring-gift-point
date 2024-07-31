package gift.service;

import gift.model.member.KakaoProperties;
import gift.repository.MemberRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class OrderServiceTest {

    @InjectMocks
    private KakaoService kakaoService;

    @Mock
    private KakaoProperties kakaoProperties;

    @Mock
    private MemberRepository memberRepository;
}