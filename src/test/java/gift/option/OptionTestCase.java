package gift.option;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class OptionTestCase {

    static class OptionQuantitySizeError implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(0),
                Arguments.of(100000000),
                Arguments.of(Integer.MAX_VALUE),
                Arguments.of(Integer.MIN_VALUE)
            );
        }
    }

    static class OptionNameLengthError implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of("thisSentenceIsTooLongUseForOptionNameDoesNotItHelloWorldHelloWorld"),
                Arguments.of("이문장은옵션의이름으로쓰기에는너무길어요안그런가요50자나되는문장을뭐라고써야할지도모르겠네요이제는딱맞는거같아요"),
                Arguments.of(
                    "공백 포함 공백 포함 공백 포함 공백 포함 [] 공백 포함 [ ] ( ) + - & / _ 공백 포함 공백 포함 공백 포함 ")
            );
        }
    }

    static class OptionNameAllowedCharacterError implements ArgumentsProvider {

        @Override
        public Stream<Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of("한글과영어 그리고 특수문자 ()[]+-&/_ 😀"),
                Arguments.of("~!@#$%^&*()_+{}|\"'")
            );
        }
    }

}
