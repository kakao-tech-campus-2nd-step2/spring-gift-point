package gift.model;

import gift.common.exception.DuplicateDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("옵션명 중복 체크[실패] - 중복된 이름")
    void checkDuplicateOptionName() {
        // given
        Category category = new Category();
        List<Option> options = List.of(
                new Option(1L,"oName"),
                new Option(2L, "oName"));

        // when
        // then
        assertThatExceptionOfType(DuplicateDataException.class)
                .isThrownBy(() -> new Product("pname", 1000, "purl", category, options));
    }

    @Test
    @DisplayName("Product의 options 필드에 모든 옵션 add 테스트[성공]")
    void createProduct() {
        // given
        Category category = new Category();
        List<Option> options = List.of(new Option("oName1", 100), new Option("oName2", 1100));

        // when
        Product product = new Product("pname", 1000, "purl", category, options);

        // then
        assertThat(product.getOptions()).hasSize(options.size());
    }

    @Test
    @DisplayName("Option에 Product가 들어가는지 테스트[성공]")
    void checkOptionInserted() {
        // given
        String oName1 = "oName1";
        String oName2 = "oName2";
        Category category = new Category();
        List<Option> options = List.of(new Option(oName1, 100), new Option(oName2, 100));
        String pname = "pname";
        int price = 1_000;
        String purl = "purl";
        // when
        Product product = new Product(pname, price, purl, category, options);

        // then
        assertThat(product.getOptions()).hasSize(options.size());
        assertThat(product.getOptions().get(0).getProduct().getName()).isEqualTo(pname);
        assertThat(product.getOptions().get(0).getProduct().getPrice()).isEqualTo(price);
        assertThat(product.getOptions().get(0).getProduct().getImageUrl()).isEqualTo(purl);

        assertThat(product.getOptions().get(0).getName()).isEqualTo(oName1);
        assertThat(product.getOptions().get(1).getName()).isEqualTo(oName2);
    }

    @Test
    @DisplayName("Product에서 Option 찾기 테스트[성공]")
    void findOptionByOptionId() {
        // given
        String oName1 = "oName1";
        String oName2 = "oName2";
        Category category = new Category();
        List<Option> options = List.of(
                new Option(1L, oName1),
                new Option(2L, oName2));
        Product product = new Product("pname", 123, "purl", category, options);

        // when
        Option option = product.findOptionByOptionId(1L);

        // then
        assertThat(option.getId()).isEqualTo(1L);
        assertThat(option.getName()).isEqualTo(oName1);
    }

    @Test
    @DisplayName("Product에서 Option 제거 테스트[성공]")
    void subOption() {
        // given
        String oName1 = "oName1";
        String oName2 = "oName2";
        Category category = new Category();
        List<Option> options = List.of(
                new Option(1L, oName1),
                new Option(2L, oName2));

        Product product = new Product("pname", 123, "purl", category, options);
        Option option = product.findOptionByOptionId(1L);

        // when
        product.subOption(option);

        // then
        assertThat(product.getOptions()).hasSize(options.size() - 1);
    }

    @Test
    @DisplayName("Product에서 Option 제거 테스트[실패]-option은 최소 1개 이상")
    void subOptionFail() {
        // given
        String oName1 = "oName1";
        Category category = new Category();
        List<Option> options = List.of(new Option(1L, oName1));
        Product product = new Product("pname", 123, "purl", category, options);
        Option option = product.findOptionByOptionId(1L);

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> product.subOption(option));
    }
}