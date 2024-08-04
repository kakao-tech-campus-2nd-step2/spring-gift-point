package gift.dto.swagger;

import gift.dto.betweenClient.option.OptionResponseDTO;
import java.util.List;

public record GetOneProductIdAllOptions(List<OptionResponseDTO> options) { }
