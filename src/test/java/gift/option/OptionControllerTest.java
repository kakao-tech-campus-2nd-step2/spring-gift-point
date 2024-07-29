package gift.option;

import static gift.exception.ErrorMessage.OPTION_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.OPTION_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.OPTION_NAME_LENGTH;
import static gift.exception.ErrorMessage.OPTION_NOT_FOUND;
import static gift.exception.ErrorMessage.OPTION_QUANTITY_SIZE;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.option.OptionTestCase.OptionNameAllowedCharacterError;
import gift.option.OptionTestCase.OptionNameLengthError;
import gift.option.OptionTestCase.OptionQuantitySizeError;
import gift.token.JwtProvider;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = OptionController.class)
public class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private OptionService optionService;

    private static final String URL = "/api/products/%d/option";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("[Unit] get option test")
    class getOptionTest {

        @Test
        @DisplayName("success")
        void success() throws Exception {
            //given
            long productId = 1L;
            List<OptionDTO> expect = List.of(
                new OptionDTO(1L, "option-1", 1),
                new OptionDTO(2L, "option-2", 2),
                new OptionDTO(3L, "option-3", 3)
            );

            //when
            when(optionService.getOptions(productId))
                .thenReturn(expect);

            //then
            mockMvc.perform(get(URL.formatted(productId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expect)));
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() throws Exception {
            //given
            long productId = 1L;

            //when
            when(optionService.getOptions(productId))
                .thenThrow(new IllegalArgumentException(PRODUCT_NOT_FOUND));

            //then
            mockMvc.perform(get(URL.formatted(productId)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NOT_FOUND));
        }
    }

    @Nested
    @DisplayName("[Unit] add option test")
    class addOptionTest {

        @Test
        @DisplayName("success")
        void success() throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, "option-1", 10);

            //when
            doNothing().when(optionService).addOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    post(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isOk())
                .andExpect(content().string(""));
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, "option-1", 10);

            //when
            doThrow(new IllegalArgumentException(PRODUCT_NOT_FOUND))
                .when(optionService)
                .addOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    post(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NOT_FOUND));
        }

        @Test
        @DisplayName("option already exist error")
        void optionAlreadyExistError() throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, "option-1", 10);

            //when
            doThrow(new IllegalArgumentException(OPTION_ALREADY_EXISTS))
                .when(optionService)
                .addOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    post(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_ALREADY_EXISTS));
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameAllowedCharacterError.class)
        @DisplayName("option name allowed character error")
        void optionNameAllowedCharacterError(String optionName) throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, optionName, 10);

            //when
            doNothing().when(optionService)
                .addOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    post(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_NAME_ALLOWED_CHARACTER));
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameLengthError.class)
        @DisplayName("option name length error")
        void optionNameLengthError(String optionName) throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, optionName, 10);

            //when
            doNothing().when(optionService)
                .addOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    post(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_NAME_LENGTH));
        }

        @ParameterizedTest
        @ArgumentsSource(OptionQuantitySizeError.class)
        @DisplayName("option quantity size error")
        void optionQuantitySizeError(int optionQuantity) throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, "option-1", optionQuantity);

            //when
            doNothing().when(optionService)
                .addOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    post(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_QUANTITY_SIZE));
        }
    }

    @Nested
    @DisplayName("[Unit] update option test")
    class updateOptionTest {

        @Test
        @DisplayName("success")
        void success() throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, "option-1", 10);

            //when
            doNothing().when(optionService)
                .updateOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    patch(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isOk())
                .andExpect(content().string(""));
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, "option-1", 10);

            //when
            doThrow(new IllegalArgumentException(PRODUCT_NOT_FOUND))
                .when(optionService)
                .updateOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    patch(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NOT_FOUND));
        }

        @Test
        @DisplayName("option not found error")
        void optionNotFoundError() throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, "option-1", 10);

            //when
            doThrow(new IllegalArgumentException(OPTION_NOT_FOUND))
                .when(optionService)
                .updateOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    patch(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_NOT_FOUND));
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameAllowedCharacterError.class)
        @DisplayName("option name allowed character error")
        void optionNameAllowedCharacterError(String optionName) throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, optionName, 10);

            //when
            doNothing().when(optionService)
                .updateOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    patch(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_NAME_ALLOWED_CHARACTER));
        }

        @ParameterizedTest
        @ArgumentsSource(OptionNameLengthError.class)
        @DisplayName("option name length error")
        void optionNameLengthError(String optionName) throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, optionName, 10);

            //when
            doNothing().when(optionService)
                .updateOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    patch(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_NAME_LENGTH));
        }

        @ParameterizedTest
        @ArgumentsSource(OptionQuantitySizeError.class)
        @DisplayName("option quantity size error")
        void optionQuantitySizeError(int optionQuantity) throws Exception {
            //given
            long productId = 1L;
            OptionDTO optionDTO = new OptionDTO(1L, "option-1", optionQuantity);

            //when
            doNothing().when(optionService)
                .updateOption(productId, optionDTO);

            //then
            mockMvc.perform(
                    patch(URL.formatted(productId))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_QUANTITY_SIZE));
        }
    }

    @Nested
    @DisplayName("[Unit] delete option test")
    class deleteOptionTest {

        @Test
        @DisplayName("success")
        void success() throws Exception {
            //given
            long productId = 1L;
            long optionId = 1L;

            //when
            doNothing().when(optionService)
                .deleteOption(productId, optionId);

            //then
            mockMvc.perform(
                    delete(URL.formatted(productId) + "/" + optionId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() throws Exception {
            //given
            long productId = 1L;
            long optionId = 1L;

            //when
            doThrow(new IllegalArgumentException(PRODUCT_NOT_FOUND))
                .when(optionService)
                .deleteOption(productId, optionId);

            //then
            mockMvc.perform(
                    delete(URL.formatted(productId) + "/" + optionId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NOT_FOUND));
        }

        @Test
        @DisplayName("option not found error")
        void optionNotFoundError() throws Exception {
            //given
            long productId = 1L;
            long optionId = 1L;

            //when
            doThrow(new IllegalArgumentException(OPTION_NOT_FOUND))
                .when(optionService)
                .deleteOption(productId, optionId);

            //then
            mockMvc.perform(
                    delete(URL.formatted(productId) + "/" + optionId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(OPTION_NOT_FOUND));
        }
    }
}
