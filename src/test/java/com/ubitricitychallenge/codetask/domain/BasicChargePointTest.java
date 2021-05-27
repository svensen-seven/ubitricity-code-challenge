package com.ubitricitychallenge.codetask.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class BasicChargePointTest {

    private ChargingPoint chargePoint;

    @BeforeEach
    void setUp() {
        chargePoint = new BasicChargingPoint();
    }

    @Test
    void testInitialOffStatus() {
        ChargingLevel chargingLevel = chargePoint.getChargingLevel();
        assertSame(ChargingLevel.OFF, chargingLevel);
    }

    @ParameterizedTest
    @EnumSource(ChargingLevel.class)
    void setChargingLevel(ChargingLevel chargingLevel) {
        chargePoint.setChargingLevel(chargingLevel);
        assertSame(chargingLevel, chargePoint.getChargingLevel());
    }

    @Test
    void testNullNotAllowed() {
        assertThrows(NullPointerException.class, () -> chargePoint.setChargingLevel(null));
    }

    @Test
    void testChargingLevelNotNull() {
        assertNotNull(chargePoint.getChargingLevel());
    }

    @Test
    void getStatus() {
        String status = chargePoint.getStatus();
        assertNotNull(status);
        assertEquals("AVAILABLE", status);
    }

}