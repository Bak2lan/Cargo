package aist.cargo.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SimpleResponseCreateDelivery {
    private String message;
    private boolean success;
    private  Long id;
    private Long userId;
    private int random;
}
