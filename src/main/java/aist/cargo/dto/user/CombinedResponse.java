package aist.cargo.dto.user;

import java.util.List;

public record CombinedResponse (
    List<CargoResponse> deliveries,
    List<SendingResponse> sendings){
}
