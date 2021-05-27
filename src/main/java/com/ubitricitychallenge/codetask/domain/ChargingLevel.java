package com.ubitricitychallenge.codetask.domain;

public enum ChargingLevel {
    FAST(20),
    SLOW(10),
    OFF(0) {
        @Override
        public String toString() {
            return "AVAILABLE";
        }
    },
    ;

    private final int consumedCurrent;

    ChargingLevel(int consumedCurrent) {
        if (consumedCurrent < 0) {
            throw new IllegalArgumentException("Consumed current should not be negative, found: " + consumedCurrent);
        }
        this.consumedCurrent = consumedCurrent;
    }

    @Override
    public String toString() {
        return "OCCUPIED " + consumedCurrent + 'A';
    }

    public int getConsumedCurrent() {
        return consumedCurrent;
    }
}
