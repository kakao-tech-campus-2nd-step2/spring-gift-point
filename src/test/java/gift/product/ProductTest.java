package gift.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("비즈니스 로직 테스트")
    @Test
    void domain(){
        //given
        String name = "카카오123";

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> new Product(null,name,1234,null, 1L));

        //then
        assertThat(exception.getMessage()).isEqualTo("\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
    }

}
