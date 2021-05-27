package com.ubitricitychallenge.codetask.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicCarparkUbiTest {

    private CarparkUbi carparkUbi;

    @BeforeEach
    void setUp() {
        carparkUbi = new BasicCarparkUbi();
    }

    @Test
    void testMissingChargingPointToConnect() {
        int chargePointsCount = carparkUbi.getChargingPointsCount();

        assertThrows(MissingChargingPointException.class, () -> carparkUbi.connectChargingPoint(chargePointsCount));
        assertThrows(MissingChargingPointException.class, () -> carparkUbi.connectChargingPoint(-1));
        assertDoesNotThrow(this::connectAllChargingPoints);
    }

    @Test
    void testMissingChargingPointToDisconnect() {
        int chargePointsCount = carparkUbi.getChargingPointsCount();

        assertThrows(MissingChargingPointException.class, () -> carparkUbi.disconnectChargingPoint(chargePointsCount));
        assertThrows(MissingChargingPointException.class, () -> carparkUbi.disconnectChargingPoint(-1));
        assertDoesNotThrow(() -> {
            for (int i = 0; i < carparkUbi.getChargingPointsCount(); i++) {
                carparkUbi.connectChargingPoint(i);
                carparkUbi.disconnectChargingPoint(i);
            }
        });
    }

    @Test
    void testDoubleConnectionIsNotAllowed() throws MissingChargingPointException, OperationForbiddenException {
        int chargingPointId = carparkUbi.getChargingPointsCount() - 1;
        carparkUbi.connectChargingPoint(chargingPointId);
        assertThrows(OperationForbiddenException.class, () -> carparkUbi.connectChargingPoint(chargingPointId));
    }

    @Test
    void testDoubleDisconnectIsNotAllowed() throws MissingChargingPointException, OperationForbiddenException {
        int chargingPointId = carparkUbi.getChargingPointsCount() - 1;
        carparkUbi.connectChargingPoint(chargingPointId);
        carparkUbi.disconnectChargingPoint(chargingPointId);
        assertThrows(OperationForbiddenException.class, () -> carparkUbi.disconnectChargingPoint(chargingPointId));
    }

    @Test
    void testInitialReport() {
        String actualReport = carparkUbi.createReport();

        String expectedReport =
                "CP1 AVAILABLE\n" +
                "CP2 AVAILABLE\n" +
                "CP3 AVAILABLE\n" +
                "CP4 AVAILABLE\n" +
                "CP5 AVAILABLE\n" +
                "CP6 AVAILABLE\n" +
                "CP7 AVAILABLE\n" +
                "CP8 AVAILABLE\n" +
                "CP9 AVAILABLE\n" +
                "CP10 AVAILABLE";
        assertEquals(expectedReport, actualReport);
    }

    @Test
    void testAllConnectedReport() throws MissingChargingPointException, OperationForbiddenException {
        connectAllChargingPoints();

        String actualReport = carparkUbi.createReport();
        String expectedReport =
                "CP1 OCCUPIED 10A\n" +
                "CP2 OCCUPIED 10A\n" +
                "CP3 OCCUPIED 10A\n" +
                "CP4 OCCUPIED 10A\n" +
                "CP5 OCCUPIED 10A\n" +
                "CP6 OCCUPIED 10A\n" +
                "CP7 OCCUPIED 10A\n" +
                "CP8 OCCUPIED 10A\n" +
                "CP9 OCCUPIED 10A\n" +
                "CP10 OCCUPIED 10A";
        assertEquals(expectedReport, actualReport);
    }

    @Test
    void testDisconnectChargingPoint() throws MissingChargingPointException, OperationForbiddenException {
        connectAllChargingPoints();

        carparkUbi.disconnectChargingPoint(5);
        String expectedReport =
                "CP1 OCCUPIED 10A\n" +
                "CP2 OCCUPIED 10A\n" +
                "CP3 OCCUPIED 10A\n" +
                "CP4 OCCUPIED 10A\n" +
                "CP5 OCCUPIED 10A\n" +
                "CP6 AVAILABLE\n" +
                "CP7 OCCUPIED 10A\n" +
                "CP8 OCCUPIED 10A\n" +
                "CP9 OCCUPIED 10A\n" +
                "CP10 OCCUPIED 20A";
        assertEquals(expectedReport, carparkUbi.createReport());

        carparkUbi.disconnectChargingPoint(0);
        expectedReport =
                "CP1 AVAILABLE\n" +
                "CP2 OCCUPIED 10A\n" +
                "CP3 OCCUPIED 10A\n" +
                "CP4 OCCUPIED 10A\n" +
                "CP5 OCCUPIED 10A\n" +
                "CP6 AVAILABLE\n" +
                "CP7 OCCUPIED 10A\n" +
                "CP8 OCCUPIED 10A\n" +
                "CP9 OCCUPIED 20A\n" +
                "CP10 OCCUPIED 20A";
        assertEquals(expectedReport, carparkUbi.createReport());

        carparkUbi.disconnectChargingPoint(9);
        expectedReport =
                "CP1 AVAILABLE\n" +
                "CP2 OCCUPIED 10A\n" +
                "CP3 OCCUPIED 10A\n" +
                "CP4 OCCUPIED 10A\n" +
                "CP5 OCCUPIED 10A\n" +
                "CP6 AVAILABLE\n" +
                "CP7 OCCUPIED 20A\n" +
                "CP8 OCCUPIED 20A\n" +
                "CP9 OCCUPIED 20A\n" +
                "CP10 AVAILABLE";
        assertEquals(expectedReport, carparkUbi.createReport());
    }

    @Test
    void testCheckpointsCount() {
        assertEquals(10, carparkUbi.getChargingPointsCount());
    }

    private void connectAllChargingPoints() throws MissingChargingPointException, OperationForbiddenException {
        for (int i = 0; i < carparkUbi.getChargingPointsCount(); i++) {
            carparkUbi.connectChargingPoint(i);
        }
    }

}