package gift.main.dto;

import gift.main.Exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OptionListRequestTest {

    /*
     * <테스트 수행 내용>
     * 중복 옵션 리스트 저장시 오류 발생 검증 테스트
     */
    @Test
    @Transactional
    void SaveInvalidOptionListTest() {
        //given
        List<OptionRequest> options = new ArrayList<>();
        options.add(new OptionRequest("3번", 122));
        options.add(new OptionRequest("3번", 455));
        options.add(new OptionRequest("3번", 4545));

        //when
        //then
        assertThatThrownBy(() -> new OptionListRequest(options))
                .isInstanceOf(CustomException.class);

    }
}