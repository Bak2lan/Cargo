package aist.cargo.enums;

import lombok.Getter;

@Getter
public enum SubsDuration {
    ONE_MONTH("99 руб"),
    THREE_MONTH("999 руб"),
    SIX_MONTH("1499 руб"),
    TWELVE_MONTH("1999 руб");

    private final String price;

    SubsDuration(String price) {
        this.price = price;
    }
}
