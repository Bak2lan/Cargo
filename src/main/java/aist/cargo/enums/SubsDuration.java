package aist.cargo.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
@Getter
public enum SubsDuration {
    @Schema(description = "1 ай, 99 руб")
    ONE_MONTH(1, "99 руб"),

    @Schema(description = "3 ай, 999 руб")
    THREE_MONTH(3, "999 руб"),

    @Schema(description = "6 ай, 1499 руб")
    SIX_MONTH(6, "1499 руб"),

    @Schema(description = "12 ай, 1999 руб")
    TWELVE_MONTH(12, "1999 руб");

    private final int months;
    private final String price;

    SubsDuration(int months, String price) {
        this.months = months;
        this.price = price;
    }

}

