package gift.global;

import com.fasterxml.jackson.annotation.JsonCreator;
import gift.domain.annotation.ValidAdminMemberArgumentResolver;
import gift.domain.annotation.ValidMemberArgumentResolver;
import gift.domain.service.member.MemberService;
import gift.global.util.JwtUtil;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public WebConfig(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ValidMemberArgumentResolver(memberService, jwtUtil));
        resolvers.add(new ValidAdminMemberArgumentResolver(memberService, jwtUtil));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .maxAge(3600);
    }

    public static class Constants {

        public static class Constraints {

            public static final String DEFAULT_ALLOWED_SPECIAL_CHARS = "()[]+-&/_";
            public static final String DEFAULT_ALLOWED_SPECIAL_MSG = "(), [], +, -, &, /, _ 이외의 특수 문자는 사용할 수 없습니다.";
        }

        public static class Domain {

            public static class Member {

                public enum Type {

                    KAKAO, LOCAL;

                    @JsonCreator
                    public static Type from(String s) {
                        return Type.valueOf(s.toUpperCase());
                    }
                }

                public enum Permission {

                    MEMBER, ADMIN;

                    @JsonCreator
                    public static Type from(String s) {
                        return Type.valueOf(s.toUpperCase());
                    }
                }
            }

            public static class Product {

                public static final int NAME_LENGTH_MIN = 1;
                public static final int NAME_LENGTH_MAX = 15;
                public static final String NAME_LENGTH_INVALID_MSG = "공백을 포함하여 최대 15자까지 입력할 수 있습니다.";
                public static final String NAME_INCLUDE_KAKAO_MSG = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.";
            }

            public static class Option {

                public static final int NAME_LENGTH_MIN = 1;
                public static final int NAME_LENGTH_MAX = 50;
                public static final int QUANTITY_RANGE_MIN = 1;
                public static final int QUANTITY_RANGE_MAX = 99_999_999;
                public static final String NAME_LENGTH_INVALID_MSG = "공백을 포함해 최대 50자까지 입력할 수 있습니다.";
                public static final String QUANTITY_INVALID_MSG = "수량은 최소 1, 최대 100,000,000 미만만 가능합니다.";

                public enum QuantityUpdateAction {

                    ADD("add"), SUBTRACT("subtract");

                    private final String action;

                    QuantityUpdateAction(String action) {
                        this.action = action;
                    }

                    public String toString() {
                        return action;
                    }

                    public static List<String> toList() {
                        return Arrays.stream(values()).map(QuantityUpdateAction::toString).toList();
                    }
                }
            }

            public static class Wish {

                public enum QuantityUpdateAction {

                    NOPE("nope"), CREATE("create"), DELETE("delete"), ADD("add");

                    private final String action;

                    QuantityUpdateAction(String action) {
                        this.action = action;
                    }

                    public String toString() {
                        return action;
                    }
                }
            }
        }
    }
}
