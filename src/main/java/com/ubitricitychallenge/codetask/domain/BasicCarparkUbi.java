package com.ubitricitychallenge.codetask.domain;

import java.util.*;

/**
 * Class representing standard CarparkUbi having 10 charging points with overall current input of 100 Amperes
 */
public final class BasicCarparkUbi implements CarparkUbi {

    private static final int CHARGE_POINTS_COUNT = 10;
    private static final int CHARGING_DELTA =
            ChargingLevel.FAST.getConsumedCurrent() - ChargingLevel.SLOW.getConsumedCurrent();

    private final ChargingPoint[] chargingPoints;
    private final List<ChargingPoint> connectedChargePoints = new LinkedList<>();
    private int availableCurrent = 100;

    public BasicCarparkUbi() {
        chargingPoints = new ChargingPoint[CHARGE_POINTS_COUNT];
        for (int i = 0; i < CHARGE_POINTS_COUNT; i++) {
            chargingPoints[i] = new BasicChargingPoint();
        }
    }

    @Override
    public void connectChargingPoint(int chargingPointId)
            throws MissingChargingPointException, OperationForbiddenException {
        checkChargingPointId(chargingPointId);
        ChargingPoint chargingPoint = chargingPoints[chargingPointId];
        if (connectedChargePoints.contains(chargingPoint)) {
            throw new OperationForbiddenException("Charging point (id = " + chargingPointId + ") is already connected");
        }

        ChargingLevel chargingLevel = ChargingLevel.SLOW;
        Iterator<ChargingPoint> iterator = connectedChargePoints.iterator();
        while (true) {
            if (availableCurrent >= ChargingLevel.FAST.getConsumedCurrent()) {
                chargingLevel = ChargingLevel.FAST;
                break;
            } else if (iterator.hasNext()) {
                ChargingPoint cp = iterator.next();
                if (cp.getChargingLevel() == ChargingLevel.FAST) {
                    cp.setChargingLevel(ChargingLevel.SLOW);
                    availableCurrent += CHARGING_DELTA;
                }
            } else {
                break;
            }
        }

        chargingPoint.setChargingLevel(chargingLevel);
        availableCurrent -= chargingLevel.getConsumedCurrent();
        connectedChargePoints.add(chargingPoint);
    }

    @Override
    public void disconnectChargingPoint(int chargingPointId)
            throws MissingChargingPointException, OperationForbiddenException {
        checkChargingPointId(chargingPointId);

        ChargingPoint connectedChargePoint = chargingPoints[chargingPointId];
        if (!connectedChargePoints.remove(connectedChargePoint)) {
            throw new OperationForbiddenException("Charging point (id = " + chargingPointId + ") is not connected");
        }

        availableCurrent += connectedChargePoint.getChargingLevel().getConsumedCurrent();
        connectedChargePoint.setChargingLevel(ChargingLevel.OFF);

        ListIterator<ChargingPoint> listIterator = connectedChargePoints.listIterator(connectedChargePoints.size());
        while (listIterator.hasPrevious() && availableCurrent >= CHARGING_DELTA) {
            ChargingPoint cp = listIterator.previous();
            if (cp.getChargingLevel() == ChargingLevel.SLOW) {
                cp.setChargingLevel(ChargingLevel.FAST);
                availableCurrent -= CHARGING_DELTA;
            }
        }
    }

    @Override
    public String createReport() {
        StringJoiner reportJoiner = new StringJoiner("\n");
        for (int i = 0; i < chargingPoints.length; i++) {
            reportJoiner.add("CP" + (i + 1) + " " + chargingPoints[i].getStatus());
        }
        return reportJoiner.toString();
    }

    @Override
    public int getChargingPointsCount() {
        return CHARGE_POINTS_COUNT;
    }

    private void checkChargingPointId(int chargingPointId) throws MissingChargingPointException {
        if (chargingPointId < 0 || chargingPointId >= CHARGE_POINTS_COUNT) {
            throw new MissingChargingPointException(chargingPointId);
        }
    }

}
