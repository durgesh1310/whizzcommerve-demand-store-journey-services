package com.ouat.checkout.exception;

public class DownStreamException extends Exception{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DownStreamException(String message) {
        super(message);
    }
    
    public DownStreamException(String message, Throwable exception) {
        super(message, exception);
    }
}
