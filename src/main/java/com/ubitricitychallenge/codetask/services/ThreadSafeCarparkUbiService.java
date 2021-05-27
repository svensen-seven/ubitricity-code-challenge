package com.ubitricitychallenge.codetask.services;

import com.ubitricitychallenge.codetask.domain.CarparkUbi;
import com.ubitricitychallenge.codetask.domain.MissingChargingPointException;
import com.ubitricitychallenge.codetask.domain.OperationForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public final class ThreadSafeCarparkUbiService implements CarparkUbiService {

    private final CarparkUbi carparkUbi;

    private final Logger logger = LoggerFactory.getLogger(ThreadSafeCarparkUbiService.class);

    public ThreadSafeCarparkUbiService(CarparkUbi carparkUbi) {
        this.carparkUbi = new ThreadSafeWrapper(carparkUbi);
    }

    @Override
    public void connectChargingPoint(int chargingPointId)
            throws MissingChargingPointException, OperationForbiddenException {
        carparkUbi.connectChargingPoint(chargingPointId);
        logger.info("Charging point (id = {}) is connected", chargingPointId);
    }

    @Override
    public void disconnectChargingPoint(int chargingPointId)
            throws MissingChargingPointException, OperationForbiddenException {
        carparkUbi.disconnectChargingPoint(chargingPointId);
        logger.info("Charging point (id = {}) is disconnected", chargingPointId);
    }

    @Override
    public String createReport() {
        String report = carparkUbi.createReport();
        logger.info("Status report is created");
        return report;
    }

    /**
     * Wrapper that uses ReadWriteLock to ensure thread-safety for CarparkUbi operations
     */
    private static final class ThreadSafeWrapper implements CarparkUbi {

        private final CarparkUbi delegate;

        private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

        public ThreadSafeWrapper(CarparkUbi delegate) {
            this.delegate = Objects.requireNonNull(delegate);
        }

        @Override
        public void connectChargingPoint(int chargingPointId)
                throws MissingChargingPointException, OperationForbiddenException {
            lock.writeLock().lock();
            try {
                delegate.connectChargingPoint(chargingPointId);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void disconnectChargingPoint(int chargingPointId)
                throws MissingChargingPointException, OperationForbiddenException {
            lock.writeLock().lock();
            try {
                delegate.disconnectChargingPoint(chargingPointId);
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public String createReport() {
            lock.readLock().lock();
            try {
                return delegate.createReport();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public int getChargingPointsCount() {
            return delegate.getChargingPointsCount();
        }

    }

}
