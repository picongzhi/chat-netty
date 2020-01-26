package com.pcz.idworker;

/**
 * @author picongzhi
 */
public class InvalidSystemClock extends RuntimeException {
    public InvalidSystemClock(String message) {
        super(message);
    }
}
