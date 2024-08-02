package gift.Controller;

import gift.DTO.OptionDTO;
import gift.Service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OptionControllerTest {

    @InjectMocks
    private OptionController optionController;

    @Mock
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOption() {
        OptionDTO optionDTO = createSampleOptionDTO();
        when(optionService.createOption(any(OptionDTO.class))).thenReturn(optionDTO);

        ResponseEntity<OptionDTO> response = optionController.createOption(optionDTO);
        assertResponse(response, HttpStatus.OK, optionDTO);
    }

    @Test
    void testGetOptionById() {
        OptionDTO optionDTO = createSampleOptionDTO();
        when(optionService.getOptionById(1L)).thenReturn(optionDTO);

        ResponseEntity<OptionDTO> response = optionController.getOptionById(1L);
        assertResponse(response, HttpStatus.OK, optionDTO);
    }

    @Test
    void testGetAllOptions() {
        List<OptionDTO> optionList = createSampleOptionList();
        when(optionService.getAllOptions()).thenReturn(optionList);

        ResponseEntity<List<OptionDTO>> response = optionController.getAllOptions();
        assertResponse(response, HttpStatus.OK, optionList);
    }

    @Test
    void testUpdateOption() {
        OptionDTO optionDTO = createSampleOptionDTO();
        when(optionService.updateOption(eq(1L), any(OptionDTO.class))).thenReturn(optionDTO);

        ResponseEntity<OptionDTO> response = optionController.updateOption(1L, optionDTO);
        assertResponse(response, HttpStatus.OK, optionDTO);
    }

    @Test
    void testDeleteOption() {
        doNothing().when(optionService).deleteOption(1L);

        ResponseEntity<Void> response = optionController.deleteOption(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(optionService, times(1)).deleteOption(1L);
    }

    private OptionDTO createSampleOptionDTO() {
        return new OptionDTO(1L, "Option1", 10L, 1L);
    }

    private List<OptionDTO> createSampleOptionList() {
        List<OptionDTO> optionList = new ArrayList<>();
        optionList.add(createSampleOptionDTO());
        return optionList;
    }

    private <T> void assertResponse(ResponseEntity<T> response, HttpStatus status, T expectedBody) {
        assertEquals(status, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }
}
