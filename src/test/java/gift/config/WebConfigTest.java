package gift.config;

import gift.resolver.LoginMemberArgumentResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class WebConfigTest {
    @Autowired
    private WebConfig webConfig;

    @Test
    void testAddArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        webConfig.addArgumentResolvers(resolvers);

        assertThat(resolvers)
                .isNotEmpty()
                .anyMatch(resolver -> resolver instanceof LoginMemberArgumentResolver);
    }
}
