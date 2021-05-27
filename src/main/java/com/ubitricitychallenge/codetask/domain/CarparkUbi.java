package com.ubitricitychallenge.codetask.domain;

/**
 * Interface that defines a set of operations for CarparkUbi
 */
public interface CarparkUbi {

    /**
     * @param chargingPointId Index of charging point to connect. Valid range is [0; chargePointsCount)
     * @throws MissingChargingPointException if {@code chargingPointId} is outside the valid range
     * @throws OperationForbiddenException if charging point is already connected
     */
    void connectChargingPoint(int chargingPointId) throws MissingChargingPointException, OperationForbiddenException;

    /**
     * @param chargingPointId Index of charging point to disconnect. Valid range is [0; chargePointsCount)
     * @throws MissingChargingPointException if {@code chargingPointId} is outside the valid range
     * @throws OperationForbiddenException if charging point is already disconnected
     */
    void disconnectChargingPoint(int chargingPointId) throws MissingChargingPointException, OperationForbiddenException;

    /**
     * Provides a report about current status of all charging points
     */
    String createReport();

    /**
     * @return total number of charging points within CarparkUbi
     */
    int getChargingPointsCount();
}
