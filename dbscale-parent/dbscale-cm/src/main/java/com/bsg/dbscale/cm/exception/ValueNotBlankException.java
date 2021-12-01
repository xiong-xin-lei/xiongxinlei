package com.bsg.dbscale.cm.exception;

public class ValueNotBlankException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ValueNotBlankException() {

    }

    public ValueNotBlankException(String msg) {
        super(msg);
    }

    public ValueNotBlankException(Throwable e) {
        super(e);
    }
}
