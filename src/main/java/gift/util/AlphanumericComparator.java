package gift.util;

import java.util.Comparator;

public class AlphanumericComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return 0;
        } else if (str1 == null) {
            return -1;
        } else if (str2 == null) {
            return 1;
        }

        int index1 = 0, index2 = 0;
        while (index1 < str1.length() && index2 < str2.length()) {
            char char1 = str1.charAt(index1);
            char char2 = str2.charAt(index2);

            if (Character.isDigit(char1) && Character.isDigit(char2)) {
                int result = compareNumbers(str1, str2, index1, index2);
                if (result != 0) {
                    return result;
                }
                index1 = moveIndexToNextNonDigit(str1, index1);
                index2 = moveIndexToNextNonDigit(str2, index2);
            }
            else {
                int result = Character.compare(char1, char2);
                if (result != 0) {
                    return result;
                }
                index1++;
                index2++;
            }
        }

        return Integer.compare(str1.length(), str2.length());
    }

    // 문자열에서의 숫자 부분 대소 비교
    private int compareNumbers(String str1, String str2, int start1, int start2) {
        int num1 = 0, num2 = 0;
        while (start1 < str1.length() && Character.isDigit(str1.charAt(start1))) {
            num1 = num1 * 10 + (str1.charAt(start1) - '0');
            start1++;
        }
        while (start2 < str2.length() && Character.isDigit(str2.charAt(start2))) {
            num2 = num2 * 10 + (str2.charAt(start2) - '0');
            start2++;
        }
        return Integer.compare(num1, num2);
    }

    // 문자열에서 숫자가 아닌 문자를 찾을때 까지 인덱스 이동
    private int moveIndexToNextNonDigit(String str, int index) {
        while (index < str.length() && Character.isDigit(str.charAt(index))) {
            index++;
        }
        return index;
    }
}
