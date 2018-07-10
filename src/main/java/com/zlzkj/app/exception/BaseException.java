package com.zlzkj.app.exception;

/**
 * Class description
 *
 *
 * @version        1.0.0, 16/03/31
 * @author         zhm    
 */
public abstract class BaseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 5718012955902143634L;

    /**
     * Constructs ...
     *
     */
    public BaseException() {
        super();
    }

    /**
     * Constructs ...
     *
     *
     * @param message
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Constructs ...
     *
     *
     * @param cause
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs ...
     *
     *
     * @param message
     * @param cause
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
