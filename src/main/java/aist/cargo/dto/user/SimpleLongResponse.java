package aist.cargo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@AllArgsConstructor
@Builder
public class SimpleLongResponse {
    private HttpStatus httpStatus;
    private String message;
    private Long id;
}
