package com.ubitricitychallenge.codetask.controllers;

import com.ubitricitychallenge.codetask.domain.MissingChargingPointException;
import com.ubitricitychallenge.codetask.domain.OperationForbiddenException;
import com.ubitricitychallenge.codetask.services.CarparkUbiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarparkUbiControllerTest {

    @InjectMocks
    private CarparkUbiController controller;

    @Mock
    private CarparkUbiService service;

    @Test
    void connectChargingPoint() throws MissingChargingPointException, OperationForbiddenException {
        int chargingPointId = 0;
        ResponseEntity<Object> responseEntity = controller.connectChargingPoint(chargingPointId);
        Mockito.verify(service, Mockito.times(1)).connectChargingPoint(chargingPointId);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void disconnectChargingPoint() throws MissingChargingPointException, OperationForbiddenException {
        int chargingPointId = 6;
        ResponseEntity<Object> responseEntity = controller.disconnectChargingPoint(chargingPointId);
        Mockito.verify(service, Mockito.times(1)).disconnectChargingPoint(chargingPointId);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getReport() {
        String allChargingPointsAvailable =
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
        Mockito.when(service.createReport()).thenReturn(allChargingPointsAvailable);
        String responseEntity = controller.getReport();
        Mockito.verify(service, Mockito.times(1)).createReport();
        assertNotNull(responseEntity);
    }

}