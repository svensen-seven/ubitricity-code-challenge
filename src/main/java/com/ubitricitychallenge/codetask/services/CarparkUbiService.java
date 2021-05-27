package com.ubitricitychallenge.codetask.services;

import com.ubitricitychallenge.codetask.domain.MissingChargingPointException;
import com.ubitricitychallenge.codetask.domain.OperationForbiddenException;

public interface CarparkUbiService {

    void connectChargingPoint(int chargingPointId) throws MissingChargingPointException, OperationForbiddenException;

    void disconnectChargingPoint(int chargingPointId) throws MissingChargingPointException, OperationForbiddenException;

    String createReport();

}
