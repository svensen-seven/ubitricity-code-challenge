package com.ubitricitychallenge.codetask.domain;

/**
 * Exception, thrown when charging point id does not exist
 */
public final class MissingChargingPointException extends Exception{

    public MissingChargingPointException(int missedChargingPointId) {
        super("The charging point (id = " + missedChargingPointId + ") is missing");
    }

}
