package gift.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StringUtilsTest {

    @ParameterizedTest(name = "{0}의 특수문자는 {1} 만을 포함하고 있어야 한다")
    @MethodSource("specialCharsSuccessCase")
    void containsOnlyAllowedSpecialCharsSuccessCase(String input, Set<Character> allowedSpecialChars) {
        assertTrue(StringUtils.containsOnlyAllowedSpecialChars(input, allowedSpecialChars));
    }

    private static Stream<Arguments> specialCharsSuccessCase() {
        return Stream.of(
            Arguments.of("abc", Set.of()),
            Arguments.of("abc", Set.of('!', '@', '#', '$', '%')),
            Arguments.of("ab!c!@#", Set.of('!', '@', '#', '$', '%')),
            Arguments.of("abc!@#%", Set.of('!', '@', '#', '$', '%')),
            Arguments.of("a@bc!#%$", Set.of('!', '@', '#', '$', '%')),
            Arguments.of("ab!c!@#%$", Set.of('!', '@', '#', '$', '%')),
            Arguments.of("ab^c!@#%$^&", Set.of('!', '@', '#', '$', '%', '^', '&')),
            Arguments.of("abc!@#%$^&*", Set.of('!', '@', '#', '$', '%', '^', '&', '*')));
    }

    @ParameterizedTest(name = "{0}의 특수문자는 {1} 외의 특수문자를 포함하고 있다")
    @MethodSource("specialCharsFailCase")
    void containsOnlyAllowedSpecialCharsFailCase(String input, Set<Character> allowedSpecialChars) {
        assertFalse(StringUtils.containsOnlyAllowedSpecialChars(input, allowedSpecialChars));
    }

    private static Stream<Arguments> specialCharsFailCase() {
        return Stream.of(
            Arguments.of("abc!", Set.of()),
            Arguments.of("a!b%!%c$", Set.of('!', '%')),
            Arguments.of("ab^c!@#%$^&)", Set.of('!', '@', '#', '$', '%', '^')),
            Arguments.of("abc!@#%$^&*[", Set.of('!', '@', '#', '$', '%', '^', '&')));
    }

    @ParameterizedTest(name = "{0}는 {1}를 포함하고 있다")
    @MethodSource("substringsSuccessCase")
    void containsAnySubstringSuccessCase(String input, Set<String> substrings) {
        assertTrue(StringUtils.containsAnySubstring(input, substrings));
    }

    private static Stream<Arguments> substringsSuccessCase() {
        return Stream.of(
            Arguments.of("abc", Set.of("ab")),
            Arguments.of("카카오 선풍기", Set.of("카카오")),
            Arguments.of("카카오", Set.of("카카오")));
    }

    @ParameterizedTest(name = "{0}는 {1}를 포함하고 있지 않다")
    @MethodSource("substringsFailCase")
    void containsAnySubstringFailCase(String input, Set<String> substrings) {
        assertFalse(StringUtils.containsAnySubstring(input, substrings));
    }

    private static Stream<Arguments> substringsFailCase() {
        return Stream.of(
            Arguments.of("abcdefg", Set.of("aefg")),
            Arguments.of("카카오 선풍기", Set.of("네이버")),
            Arguments.of("카카오 선풍기", Set.of("카카오선풍기"))
            );
    }
}