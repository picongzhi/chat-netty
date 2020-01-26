package com.pcz.idworker.strategy;

/**
 * @author picongzhi
 */
public interface RandomCodeStrategy {
    void init();

    int prefix();

    int next();

    void release();
}
