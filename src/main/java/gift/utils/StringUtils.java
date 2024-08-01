package gift.utils;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class StringUtils {

    /**
     * 문자열과 문자열 집합을 전달받아, 집합에 포함된 어떤 문자열이라도 입력 문자열에 포함되어 있는지 검사합니다.
     *
     * @param input 검사할 문자열
     * @param substrings 검사할 문자열 집합
     * @return 집합에 포함된 어떤 문자열이라도 입력 문자열에 포함되어 있으면 true, 그렇지 않으면 false
     */
    public static boolean containsAnySubstring(String input, Set<String> substrings) {
        return substrings.stream().anyMatch(input::contains);
    }

    /**
     * 문자열과 허용할 특수문자의 목록을 받아서 문자열이 허용할 특수문자 외의 특수문자가 있는지 검사합니다.
     *
     * @param input 문자열 입력
     * @param allowedSpecialChars 허용할 특수문자의 목록
     * @return 허용되지 않는 특수문자가 있으면 false, 그렇지 않으면 true
     */
    public static boolean containsOnlyAllowedSpecialChars(String input, Set<Character> allowedSpecialChars) {
        String allowedCharsRegex = allowedSpecialChars.stream()
            .map(ch -> "\\" + ch)
            .collect(Collectors.joining());

        String regex = "[a-zA-Z0-9ㄱ-ㅎ가-힣ㅏ-ㅣ\\s" + allowedCharsRegex + "]+";
        return input.matches(regex);
    }
}
