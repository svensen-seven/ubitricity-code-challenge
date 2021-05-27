package com.ubitricitychallenge.codetask.domain;

/**
 * Exception thrown when operation could not be performed
 */
public final class OperationForbiddenException extends Exception {

    public OperationForbiddenException(String reason) {
        super(reason);
    }

}
