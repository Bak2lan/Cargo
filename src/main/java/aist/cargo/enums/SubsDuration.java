package aist.cargo.enums;

import lombok.Getter;

@Getter
public enum SubsDuration {
    ONE_MONTH(1, "99 руб"),
    THREE_MONTH(3, "999 руб"),
    SIX_MONTH(6, "1499 руб"),
    TWELVE_MONTH(12, "1999 руб");
    
    private final int months;
    private final String price;

    SubsDuration(int months, String price) {
        this.months = months;
        this.price = price;
    }

}
