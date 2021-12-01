package com.bsg.dbscale.cm.exception;

public class CallingInterfaceException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CallingInterfaceException() {

    }

    public CallingInterfaceException(String msg) {
        super(msg);
    }

    public CallingInterfaceException(Throwable e) {
        super(e);
    }
}
