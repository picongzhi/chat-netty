package com.pcz.idworker.strategy;

/**
 * @author picongzhi
 */
public interface WorkerIdStrategy {
    void initialize();

    long availableWorkerId();

    void release();
}
