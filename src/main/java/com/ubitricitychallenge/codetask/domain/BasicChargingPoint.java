package com.ubitricitychallenge.codetask.domain;

import java.util.Objects;

public final class BasicChargingPoint implements ChargingPoint {

    private ChargingLevel chargingLevel = ChargingLevel.OFF;

    @Override
    public void setChargingLevel(ChargingLevel chargingLevel) {
        this.chargingLevel = Objects.requireNonNull(chargingLevel, "Charging level could not be null");
    }

    @Override
    public ChargingLevel getChargingLevel() {
        return chargingLevel;
    }

    @Override
    public String getStatus() {
        return chargingLevel.toString();
    }

}
