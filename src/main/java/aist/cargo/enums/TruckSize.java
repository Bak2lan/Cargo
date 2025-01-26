package aist.cargo.enums;

import lombok.Getter;

@Getter
public enum TruckSize {
    SMALL("Малый кузов", 300),
    MEDIUM("Средний кузов", 1000),
    LARGE("Большой кузов", 5000),
    TRUCK("Фура", 20000);

    private final String description;
    private final int maxWeight;

    TruckSize(String description, int maxWeight) {
        this.description = description;
        this.maxWeight = maxWeight;
    }

}
