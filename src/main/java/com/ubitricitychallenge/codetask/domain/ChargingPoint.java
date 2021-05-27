package com.ubitricitychallenge.codetask.domain;

/**
 * Interface that defines operations for Charging Point
 */
public interface ChargingPoint {

    /**
     * @param chargingLevel charging level to which a charging point should be switched
     */
    void setChargingLevel(ChargingLevel chargingLevel);

    /**
     * @return current charging level
     */
    ChargingLevel getChargingLevel();

    /**
     * @return the current status of the charging point
     */
    String getStatus();

}
